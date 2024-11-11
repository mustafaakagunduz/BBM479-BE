package com.sms.hrsam.service;

import com.sms.hrsam.dto.SurveyResultDTO;
import com.sms.hrsam.dto.ProfessionMatchDTO;
import com.sms.hrsam.entity.*;
import com.sms.hrsam.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class SurveyResultService {
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final ResponseRepository responseRepository;
    private final RequiredLevelRepository requiredLevelRepository;

    @Transactional
    public SurveyResultDTO calculateAndSaveSurveyResult(Long surveyId, Long userId) {
        try {
            // Önce mevcut sonucu kontrol et
            Optional<SurveyResult> existingResult = surveyResultRepository.findBySurveyIdAndUserId(surveyId, userId);
            if (existingResult.isPresent()) {
                return mapToDTO(existingResult.get());
            }

            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new RuntimeException("Survey not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Kullanıcının cevaplarını al
            List<Response> userResponses = responseRepository.findBySurveyIdAndUserId(surveyId, userId);

            if (userResponses.isEmpty()) {
                throw new RuntimeException("No responses found for this survey");
            }

            // Kullanıcının skill seviyelerini maple
            Map<Long, Integer> userSkillLevels = new HashMap<>();
            for (Response response : userResponses) {
                Long skillId = response.getQuestion().getSkill().getId();
                Integer level = response.getEnteredLevel();
                userSkillLevels.put(skillId, level);
            }

            List<ProfessionMatch> professionMatches = new ArrayList<>();

            for (Profession profession : survey.getProfessions()) {
                List<RequiredLevel> requiredLevels = requiredLevelRepository.findByProfessionId(profession.getId());

                if (requiredLevels.isEmpty()) {
                    continue; // Bu profession için required level yoksa atla
                }

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

                ProfessionMatch professionMatch = new ProfessionMatch();
                professionMatch.setProfession(profession);
                professionMatch.setMatchPercentage(Math.min(100, Math.max(0, matchPercentage)));
                professionMatches.add(professionMatch);
            }

            // Sonuçları kaydet
            SurveyResult surveyResult = new SurveyResult();
            surveyResult.setUser(user);
            surveyResult.setSurvey(survey);
            surveyResult.setCreatedAt(LocalDateTime.now());
            surveyResult.setProfessionMatches(professionMatches);

            professionMatches.forEach(match -> match.setSurveyResult(surveyResult));

            SurveyResult savedResult = surveyResultRepository.save(surveyResult);

            return mapToDTO(savedResult);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating survey result: " + e.getMessage(), e);
        }
    }

    public SurveyResultDTO getSurveyResult(Long surveyId, Long userId) {
        SurveyResult result = surveyResultRepository.findBySurveyIdAndUserId(surveyId, userId)
                .orElseThrow(() -> new RuntimeException("Survey result not found"));
        return mapToDTO(result);
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