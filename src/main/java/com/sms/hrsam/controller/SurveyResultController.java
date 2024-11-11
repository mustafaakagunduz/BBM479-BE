// SurveyResultController.java
package com.sms.hrsam.controller;

import com.sms.hrsam.ErrorResponse;
import com.sms.hrsam.dto.SurveyResultDTO;
import com.sms.hrsam.service.ResponseService;
import com.sms.hrsam.service.SurveyResultService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class SurveyResultController {

    private final SurveyResultService surveyResultService;

    private final ResponseService responseService; // Response kontrolü için

    @GetMapping("/{surveyId}/responses/check/{userId}")
    public ResponseEntity<?> checkSurveyCompletion(@PathVariable Long surveyId, @PathVariable Long userId) {
        boolean isCompleted = responseService.isSurveyCompletedByUser(surveyId, userId);
        return ResponseEntity.ok(Map.of("completed", isCompleted));
    }

    @PostMapping("/{surveyId}/results/{userId}/calculate")
    public ResponseEntity<SurveyResultDTO> calculateResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId,
            HttpServletRequest request) {
        // İsteğin zaten işleniyor olup olmadığını kontrol et
        String requestKey = "calculating_" + surveyId + "_" + userId;
        Object mutex = request.getSession().getAttribute(requestKey);

        if (mutex != null) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(null);
        }

        try {
            request.getSession().setAttribute(requestKey, true);
            log.info("Calculating result for surveyId: {} and userId: {}", surveyId, userId);
            SurveyResultDTO result = surveyResultService.calculateAndSaveSurveyResult(surveyId, userId);
            return ResponseEntity.ok(result);
        } finally {
            request.getSession().removeAttribute(requestKey);
        }
    }

    @GetMapping("/{surveyId}/results/{userId}")
    public ResponseEntity<SurveyResultDTO> getResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId) {
        log.info("Fetching result for surveyId: {} and userId: {}", surveyId, userId);
        SurveyResultDTO result = surveyResultService.getSurveyResult(surveyId, userId);
        return ResponseEntity.ok(result);
    }


}