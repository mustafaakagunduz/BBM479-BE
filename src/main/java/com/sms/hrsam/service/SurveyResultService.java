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

import java.util.concurrent.TimeUnit;
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
    public SurveyResultDTO calculateAndSaveSurveyResult(Long surveyId, Long userId, Boolean force) {
        String lockKey = String.format("calc_%d_%d", surveyId, userId);
        Lock calculationLock = calculationLocks.computeIfAbsent(lockKey, k -> new ReentrantLock());

        boolean lockAcquired = false;
        try {
            // Try to acquire lock with a 30-second timeout (increased from 10)
            lockAcquired = calculationLock.tryLock(30, TimeUnit.SECONDS);

            if (!lockAcquired) {
                log.warn("Calculation still in progress after timeout for surveyId: {} and userId: {}", surveyId, userId);
                throw new RuntimeException("Calculation is taking longer than expected. Please try again in a moment.");
            }

            log.info("Starting calculation for surveyId: {} and userId: {}", surveyId, userId);

            // Check if the user has already completed this survey
            Optional<SurveyResult> existingResult = surveyResultRepository
                    .findFirstBySurveyIdAndUserIdOrderByAttemptNumberDesc(surveyId, userId);

            // Always recalculate if force is true
            if (Boolean.TRUE.equals(force)) {
                log.info("Force parameter is true. Creating a new calculation for surveyId: {} and userId: {}", surveyId, userId);
            }
            // Otherwise, return existing result if present
            else if (existingResult.isPresent()) {
                log.info("Returning existing result for surveyId: {} and userId: {}", surveyId, userId);
                return mapToDTO(existingResult.get());
            }

            // Check if the user has submitted responses for this survey
            List<Response> userResponses = responseRepository.findBySurveyIdAndUserId(surveyId, userId);
            if (userResponses.isEmpty()) {
                log.warn("No responses found for surveyId: {} and userId: {}", surveyId, userId);
                throw new RuntimeException("No responses found for this survey. Please answer all questions first.");
            }

            // Fetch survey and user entities
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new RuntimeException("Survey not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Prepare user skill levels from responses
            Map<Long, Integer> userSkillLevels = userResponses.stream()
                    .collect(Collectors.toMap(
                            response -> response.getQuestion().getSkill().getId(),
                            Response::getEnteredLevel,
                            (existing, replacement) -> replacement
                    ));

            // Create new SurveyResult
            SurveyResult surveyResult = new SurveyResult();
            surveyResult.setUser(user);
            surveyResult.setSurvey(survey);
            surveyResult.setCreatedAt(LocalDateTime.now());

            // Set attempt number based on existing result or as new attempt
            int attemptNumber = existingResult.isPresent()
                    ? existingResult.get().getAttemptNumber() + 1
                    : 1;
            surveyResult.setAttemptNumber(attemptNumber);
            surveyResult.setProfessionMatches(new ArrayList<>());

            // Calculate profession matches
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

            try {
                // Save the result
                SurveyResult savedResult = surveyResultRepository.save(surveyResult);
                log.info("Successfully saved new survey result with id: {} for attempt: {}",
                        savedResult.getId(), attemptNumber);
                return mapToDTO(savedResult);
            } catch (Exception e) {
                log.error("Error saving survey result: ", e);
                throw new RuntimeException("Failed to save survey result: " + e.getMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while waiting for calculation lock: {}", e.getMessage());
            throw new RuntimeException("Calculation was interrupted, please try again.");
        } finally {
            if (lockAcquired) {
                calculationLock.unlock();
                cleanupLocks();
            }
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
        dto.setSurveyTitle(result.getSurvey().getTitle());  // Survey title'ı eklendi
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
                    // İlgili Response'ları sil
                    List<Response> responses = responseRepository.findBySurveyIdAndUserId(
                            result.getSurvey().getId(),
                            result.getUser().getId()
                    );
                    responseRepository.deleteAll(responses);

                    // ProfessionMatch'leri temizle
                    result.getProfessionMatches().forEach(match -> {
                        match.setSurveyResult(null);
                        match.setProfession(null);
                    });
                    professionMatchRepository.deleteAll(result.getProfessionMatches());
                    result.getProfessionMatches().clear();

                    // QuestionResult'ları temizle
                    result.getQuestionResults().clear();

                    // SurveyResult'ı sil
                    surveyResultRepository.delete(result);

                    log.info("Successfully deleted result with id: {} and its related responses", resultId);
                });
    }

}