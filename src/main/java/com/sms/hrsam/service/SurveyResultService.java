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

    @Transactional
    public SurveyResultDTO calculateAndSaveSurveyResult(Long surveyId, Long userId) {
        synchronized(lock) {  // Eşzamanlı işlemleri engelle
            try {
                log.info("Starting calculation for surveyId: {} and userId: {}", surveyId, userId);

                // Önce tüm mevcut sonuçları sil
                List<SurveyResult> existingResults = surveyResultRepository.findAllBySurveyIdAndUserId(surveyId, userId);
                if (!existingResults.isEmpty()) {
                    log.info("Found {} existing results, deleting them all", existingResults.size());
                    surveyResultRepository.deleteAll(existingResults);
                    surveyResultRepository.flush();
                }

                Survey survey = surveyRepository.findById(surveyId)
                        .orElseThrow(() -> new RuntimeException("Survey not found"));

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // User responses'ları al
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

                // Profession matches'leri hesapla ve ekle
                survey.getProfessions().forEach(profession -> {
                    List<RequiredLevel> requiredLevels = requiredLevelRepository.findByProfessionId(profession.getId());

                    if (!requiredLevels.isEmpty()) {
                        double totalScore = 0.0;
                        int totalRequirements = requiredLevels.size();

                        for (RequiredLevel requirement : requiredLevels) {
                            Long skillId = requirement.getSkill().getId();
                            Integer userLevel = userSkillLevels.get(skillId);
                            Integer requiredLevel = requirement.getRequiredLevel();

                            if (userLevel != null && requiredLevel != null) {
                                double score = Math.min(userLevel, requiredLevel) / (double) requiredLevel;
                                totalScore += score;
                            }
                        }

                        double matchPercentage = (totalScore / totalRequirements) * 100;
                        matchPercentage = Math.min(100, Math.max(0, matchPercentage));

                        // Yuvarlama işlemi - virgülden sonra 2 basamak
                        matchPercentage = Math.round(matchPercentage * 100.0) / 100.0;

                        ProfessionMatch professionMatch = new ProfessionMatch();
                        professionMatch.setProfession(profession);
                        professionMatch.setMatchPercentage(matchPercentage);
                        professionMatch.setSurveyResult(surveyResult);

                        surveyResult.getProfessionMatches().add(professionMatch);
                    }
                });

                // Sonucu kaydet
                SurveyResult savedResult = surveyResultRepository.save(surveyResult);
                log.info("Saved new survey result with id: {}", savedResult.getId());

                return mapToDTO(savedResult);

            } catch (Exception e) {
                log.error("Error calculating survey result:", e);
                throw new RuntimeException("Failed to calculate survey result: " + e.getMessage());
            }
        }
    }


    public SurveyResultDTO getSurveyResult(Long surveyId, Long userId) {
        List<SurveyResult> results = surveyResultRepository.findAllBySurveyIdAndUserId(surveyId, userId);
        if (results.isEmpty()) {
            throw new RuntimeException("Survey result not found");
        }
        // En son oluşturulan sonucu döndür
        return mapToDTO(results.stream()
                .max(Comparator.comparing(SurveyResult::getCreatedAt))
                .orElseThrow(() -> new RuntimeException("Survey result not found")));
    }

    private SurveyResultDTO mapToDTO(SurveyResult result) {
        SurveyResultDTO dto = new SurveyResultDTO();
        dto.setId(result.getId());
        dto.setUserId(result.getUser().getId());
        dto.setSurveyId(result.getSurvey().getId());
        dto.setCreatedAt(result.getCreatedAt());

        dto.setProfessionMatches(result.getProfessionMatches().stream()
                .map(this::mapToProfessionMatchDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private ProfessionMatchDTO mapToProfessionMatchDTO(ProfessionMatch match) {
        ProfessionMatchDTO dto = new ProfessionMatchDTO();
        dto.setProfessionId(match.getProfession().getId());
        dto.setProfessionName(match.getProfession().getName());
        dto.setMatchPercentage(match.getMatchPercentage());
        return dto;
    }
}