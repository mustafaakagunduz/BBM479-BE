package com.sms.hrsam.service;

import com.sms.hrsam.entity.Profession;
import com.sms.hrsam.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SurveyResultDebugHelper {

    public void logCalculationStart(Long surveyId, Long userId) {
        log.info("Starting survey result calculation for surveyId: {} and userId: {}", surveyId, userId);
    }

    public void logInfo(String message) {
        log.info(message);
    }

    public void logUserResponses(List<Response> responses) {
        log.info("Found {} user responses", responses.size());
        responses.forEach(response ->
                log.debug("Response for skillId: {}, level: {}",
                        response.getQuestion().getSkill().getId(),
                        response.getEnteredLevel())
        );
    }

    public void logProfessionCalculation(Profession profession, double matchPercentage) {
        log.info("Calculated match for profession {}: {}%",
                profession.getName(),
                String.format("%.2f", matchPercentage));
    }

    public void logError(String message, Exception e) {
        log.error("Error in survey result calculation: {} - {}", message, e.getMessage());
        log.error("Stack trace:", e);
    }
}