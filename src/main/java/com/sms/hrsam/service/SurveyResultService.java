package com.sms.hrsam.service;

import com.sms.hrsam.dto.SurveyResultDTO;
import com.sms.hrsam.dto.ProfessionMatchDTO;
import com.sms.hrsam.entity.*;
import com.sms.hrsam.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyResultService {
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final ResponseRepository responseRepository;
    private final RequiredLevelRepository requiredLevelRepository;

    private static final Object lock = new Object();

    public Optional<SurveyResultDTO> findSurveyResultById(Long resultId) {
        log.info("Fetching survey result by id: {}", resultId);
        return surveyResultRepository.findById(resultId)
                .map(this::mapToDTO);
    }

    @Transactional
    public SurveyResultDTO calculateAndSaveSurveyResult(Long surveyId, Long userId) {
        synchronized(lock) {
            try {
                log.info("Starting calculation for surveyId: {} and userId: {}", surveyId, userId);

                // En son deneme numarasını bul
                Integer lastAttemptNumber = surveyResultRepository.findMaxAttemptNumberBySurveyIdAndUserId(surveyId, userId)
                        .orElse(0);

                Survey survey = surveyRepository.findById(surveyId)
                        .orElseThrow(() -> new RuntimeException("Survey not found"));

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // En son responses'ları al
                List<Response> userResponses = responseRepository.findBySurveyIdAndUserId(surveyId, userId);
                if (userResponses.isEmpty()) {
                    throw new RuntimeException("No responses found for this survey");
                }

                // Skill levels'ları maple
                Map<Long, Integer> userSkillLevels = new HashMap<>();
                for (Response response : userResponses) {
                    Long skillId = response.getQuestion().getSkill().getId();
                    Integer level = response.getEnteredLevel();
                    userSkillLevels.put(skillId, level);
                }

                // Yeni SurveyResult oluştur
                SurveyResult surveyResult = new SurveyResult();
                surveyResult.setUser(user);
                surveyResult.setSurvey(survey);
                surveyResult.setCreatedAt(LocalDateTime.now());
                surveyResult.setAttemptNumber(lastAttemptNumber + 1);
                surveyResult.setProfessionMatches(new ArrayList<>()); // Initialize the list

                // İlk kaydet
                final SurveyResult finalSurveyResult = surveyResultRepository.save(surveyResult);

                // Profession matches'leri hesapla ve ekle
                survey.getProfessions().forEach(profession -> {
                    List<RequiredLevel> requiredLevels = requiredLevelRepository.findByProfessionId(profession.getId());
                    if (!requiredLevels.isEmpty()) {
                        double totalScore = calculateTotalScore(requiredLevels, userSkillLevels);
                        double matchPercentage = calculateMatchPercentage(totalScore, requiredLevels.size());

                        ProfessionMatch professionMatch = new ProfessionMatch();
                        professionMatch.setProfession(profession);
                        professionMatch.setMatchPercentage(matchPercentage);
                        finalSurveyResult.addProfessionMatch(professionMatch);
                    }
                });

                // Son kez kaydet
                SurveyResult savedResult = surveyResultRepository.save(finalSurveyResult);

                log.info("Saved new survey result with id: {} and attempt: {}",
                        savedResult.getId(), savedResult.getAttemptNumber());

                return mapToDTO(savedResult);

            } catch (Exception e) {
                log.error("Error calculating survey result:", e);
                throw new RuntimeException("Failed to calculate survey result: " + e.getMessage());
            }
        }
    }
    private double calculateTotalScore(List<RequiredLevel> requiredLevels, Map<Long, Integer> userSkillLevels) {
        return requiredLevels.stream()
                .filter(requirement -> userSkillLevels.containsKey(requirement.getSkill().getId()))
                .mapToDouble(requirement -> {
                    Integer userLevel = userSkillLevels.get(requirement.getSkill().getId());
                    Integer requiredLevel = requirement.getRequiredLevel();
                    return Math.min(userLevel, requiredLevel) / (double) requiredLevel;
                })
                .sum();
    }

    private double calculateMatchPercentage(double totalScore, int totalRequirements) {
        double matchPercentage = (totalScore / totalRequirements) * 100;
        matchPercentage = Math.min(100, Math.max(0, matchPercentage));
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
}