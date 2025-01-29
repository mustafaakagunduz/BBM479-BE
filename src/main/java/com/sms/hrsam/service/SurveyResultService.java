package com.sms.hrsam.service;

import com.sms.hrsam.dto.SurveyResultDTO;
import com.sms.hrsam.dto.ProfessionMatchDTO;
import com.sms.hrsam.entity.*;
import com.sms.hrsam.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class SurveyResultService {
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final ResponseRepository responseRepository;
    private final RequiredLevelRepository requiredLevelRepository;
    private final ProfessionMatchRepository professionMatchRepository;
    private static final Logger log = LoggerFactory.getLogger(SurveyResultService.class);
    private static final Map<String, Lock> calculationLocks = new ConcurrentHashMap<>();
    private static final Object lock = new Object();

    public Optional<SurveyResultDTO> findSurveyResultById(Long resultId) {
        log.info("Fetching survey result by id: {}", resultId);
        return surveyResultRepository.findById(resultId)
                .map(this::mapToDTO);
    }

    @Transactional
    public SurveyResultDTO calculateAndSaveSurveyResult(Long surveyId, Long userId) {
        String lockKey = String.format("calc_%d_%d", surveyId, userId);
        Lock calculationLock = calculationLocks.computeIfAbsent(lockKey, k -> new ReentrantLock());

        if (!calculationLock.tryLock()) {
            log.warn("Calculation already in progress for surveyId: {} and userId: {}", surveyId, userId);
            throw new RuntimeException("Another calculation is in progress");
        }

        try {
            synchronized (lockKey.intern()) {
                log.info("Starting calculation for surveyId: {} and userId: {}", surveyId, userId);

                // Son 2 saniye içinde oluşturulmuş sonuç var mı kontrol et
                Optional<SurveyResult> recentResult = surveyResultRepository
                        .findRecentBySurveyIdAndUserId(
                                surveyId,
                                userId,
                                LocalDateTime.now().minusSeconds(2)
                        );

                if (recentResult.isPresent()) {
                    log.info("Recent result found, returning existing result with id: {} and attempt: {}",
                            recentResult.get().getId(), recentResult.get().getAttemptNumber());
                    return mapToDTO(recentResult.get());
                }

                // Anketin tamamlanıp tamamlanmadığını kontrol et
                List<Response> userResponses = responseRepository.findBySurveyIdAndUserId(surveyId, userId);
                if (userResponses.isEmpty()) {
                    throw new RuntimeException("No responses found for this survey");
                }

                // En son attempt numarasını al
                Integer lastAttemptNumber = surveyResultRepository
                        .findMaxAttemptNumberBySurveyIdAndUserId(surveyId, userId)
                        .orElse(0);

                // Survey ve User varlığını kontrol et
                Survey survey = surveyRepository.findById(surveyId)
                        .orElseThrow(() -> new RuntimeException("Survey not found"));

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // Skill levels'ları hazırla
                Map<Long, Integer> userSkillLevels = userResponses.stream()
                        .collect(Collectors.toMap(
                                response -> response.getQuestion().getSkill().getId(),
                                Response::getEnteredLevel,
                                (existing, replacement) -> replacement
                        ));

                // Son bir kez daha attempt numarasını kontrol et
                Integer currentAttemptNumber = surveyResultRepository
                        .findMaxAttemptNumberBySurveyIdAndUserId(surveyId, userId)
                        .orElse(0);

                if (currentAttemptNumber > lastAttemptNumber) {
                    lastAttemptNumber = currentAttemptNumber;
                    log.info("Attempt number updated during calculation, using new value: {}", lastAttemptNumber);
                }

                // SurveyResult nesnesini oluştur
                SurveyResult surveyResult = new SurveyResult();
                surveyResult.setUser(user);
                surveyResult.setSurvey(survey);
                surveyResult.setCreatedAt(LocalDateTime.now());
                surveyResult.setAttemptNumber(lastAttemptNumber + 1);
                surveyResult.setProfessionMatches(new ArrayList<>());

                // Profession matches'leri hesapla
                survey.getProfessions().forEach(profession -> {
                    List<RequiredLevel> requiredLevels = requiredLevelRepository
                            .findByProfessionId(profession.getId());

                    if (!requiredLevels.isEmpty()) {
                        double totalScore = calculateTotalScore(requiredLevels, userSkillLevels);
                        double matchPercentage = calculateMatchPercentage(totalScore, requiredLevels.size());

                        ProfessionMatch professionMatch = new ProfessionMatch();
                        professionMatch.setProfession(profession);
                        professionMatch.setMatchPercentage(matchPercentage);
                        surveyResult.addProfessionMatch(professionMatch);
                    }
                });

                // Son bir kez daha kontrol et
                Optional<SurveyResult> finalCheck = surveyResultRepository
                        .findRecentBySurveyIdAndUserId(
                                surveyId,
                                userId,
                                surveyResult.getCreatedAt().minusSeconds(1)
                        );

                if (finalCheck.isPresent()) {
                    log.info("Result was created by another thread during calculation, returning that result");
                    return mapToDTO(finalCheck.get());
                }

                try {
                    // Transactional olarak kaydet
                    SurveyResult savedResult = surveyResultRepository.save(surveyResult);
                    log.info("Successfully saved new survey result with id: {} and attempt: {}",
                            savedResult.getId(), savedResult.getAttemptNumber());
                    return mapToDTO(savedResult);
                } catch (Exception e) {
                    log.error("Error saving survey result: ", e);
                    throw new RuntimeException("Failed to save survey result: " + e.getMessage());
                }
            }
        } finally {
            calculationLock.unlock();
            // Eski kilitleri temizle
            cleanupLocks();
        }
    }

    private void cleanupLocks() {
        if (calculationLocks.size() > 1000) {
            calculationLocks.clear();
        }
    }

    private double calculateTotalScore(List<RequiredLevel> requiredLevels, Map<Long, Integer> userSkillLevels) {
        return requiredLevels.stream()
                .filter(requirement -> userSkillLevels.containsKey(requirement.getSkill().getId()))
                .mapToDouble(requirement -> {
                    Integer userLevel = userSkillLevels.get(requirement.getSkill().getId());
                    Integer requiredLevel = requirement.getRequiredLevel();
                    // Her bir skill için yüzdelik hesaplama
                    return (userLevel.doubleValue() / requiredLevel.doubleValue()) * 100;
                })
                .sum();
    }

    private double calculateMatchPercentage(double totalScore, int totalRequirements) {
        // totalScore zaten yüzdelik olarak geldiği için sadece ortalamasını alıyoruz
        double matchPercentage = totalScore / totalRequirements;
        // 0-100 aralığında olduğundan emin oluyoruz
        matchPercentage = Math.min(100, Math.max(0, matchPercentage));
        // İki ondalık basamağa yuvarlama
        return Math.round(matchPercentage * 100.0) / 100.0;
    }


    public Optional<SurveyResultDTO> findLatestSurveyResult(Long surveyId, Long userId) {
        return surveyResultRepository
                .findFirstBySurveyIdAndUserIdOrderByAttemptNumberDesc(surveyId, userId)
                .map(this::mapToDTO);
    }

    public List<SurveyResultDTO> getAllSurveyResults(Long surveyId, Long userId) {
        return surveyResultRepository
                .findAllBySurveyIdAndUserIdOrderByAttemptNumberDesc(surveyId, userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SurveyResultDTO> findSurveyResultByAttempt(Long surveyId, Long userId, Integer attemptNumber) {
        return surveyResultRepository
                .findBySurveyIdAndUserIdAndAttemptNumber(surveyId, userId, attemptNumber)
                .map(this::mapToDTO);
    }

    private SurveyResultDTO mapToDTO(SurveyResult result) {
        SurveyResultDTO dto = new SurveyResultDTO();
        dto.setId(result.getId());
        dto.setUserId(result.getUser().getId());
        dto.setSurveyId(result.getSurvey().getId());
        dto.setCreatedAt(result.getCreatedAt());
        dto.setAttemptNumber(result.getAttemptNumber());

        dto.setProfessionMatches(result.getProfessionMatches().stream()
                .map(this::mapToProfessionMatchDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private ProfessionMatchDTO mapToProfessionMatchDTO(ProfessionMatch match) {
        ProfessionMatchDTO dto = new ProfessionMatchDTO();
        dto.setId(match.getId());         // Yeni eklenen
        dto.setProfessionId(match.getProfession().getId());
        dto.setProfessionName(match.getProfession().getName());
        dto.setMatchPercentage(match.getMatchPercentage());
        return dto;
    }
    // SurveyResultService'e eklenecek metotlar

    public List<SurveyResultDTO> getAllResultsByUserId(Long userId) {
        log.info("Fetching all results for userId: {}", userId);
        return surveyResultRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId)  // Bu metodu repository'ye de eklememiz gerekecek
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteResult(Long resultId) {
        log.info("Deleting result with id: {}", resultId);
        surveyResultRepository.findById(resultId)
                .ifPresent(result -> {
                    surveyResultRepository.delete(result);
                    log.info("Successfully deleted result with id: {}", resultId);
                });
    }
    @Transactional
    public void deleteSurveyResults(List<Long> surveyResultIds) {
        try {
            for (Long resultId : surveyResultIds) {
                SurveyResult result = surveyResultRepository.findById(resultId)
                        .orElseThrow(() -> new RuntimeException("Survey result not found with id: " + resultId));

                // 1. First clear profession matches
                result.getProfessionMatches().forEach(match -> {
                    match.setSurveyResult(null);
                    match.setProfession(null);
                });
                professionMatchRepository.deleteAll(result.getProfessionMatches());
                result.getProfessionMatches().clear();

                // 2. Clear question results
                result.getQuestionResults().clear();

                // 3. Save the cleared result
                surveyResultRepository.save(result);

                // 4. Now delete the survey result
                // 4. Now delete the survey resultcalc
                surveyResultRepository.delete(result);
            }

            log.info("Successfully deleted {} survey results", surveyResultIds.size());
        } catch (Exception e) {
            log.error("Error deleting survey results: {}", e.getMessage());
            throw new RuntimeException("Failed to delete survey results: " + e.getMessage());
        }
    }
}