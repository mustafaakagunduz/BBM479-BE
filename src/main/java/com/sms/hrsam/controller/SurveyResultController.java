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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class SurveyResultController {

    private final SurveyResultService surveyResultService;
    private final ResponseService responseService;

    @GetMapping("/{surveyId}/responses/check/{userId}")
    public ResponseEntity<?> checkSurveyCompletion(@PathVariable Long surveyId, @PathVariable Long userId) {
        log.info("Checking survey completion for surveyId: {} and userId: {}", surveyId, userId);
        boolean isCompleted = responseService.isSurveyCompletedByUser(surveyId, userId);
        log.info("Survey completion status: {}", isCompleted);

        return ResponseEntity.ok(Map.of(
                "completed", isCompleted,
                "message", isCompleted ? "Survey is completed" : "Please complete the survey first"
        ));
    }

    @PostMapping("/{surveyId}/results/{userId}/calculate")
    public ResponseEntity<SurveyResultDTO> calculateResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId,
            HttpServletRequest request) {

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

    @GetMapping("/{surveyId}/results/{userId}/latest")
    public ResponseEntity<SurveyResultDTO> getLatestResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId) {
        log.info("Fetching latest result for surveyId: {} and userId: {}", surveyId, userId);
        SurveyResultDTO result = surveyResultService.getLatestSurveyResult(surveyId, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{surveyId}/results/{userId}/all")
    public ResponseEntity<List<SurveyResultDTO>> getAllResults(
            @PathVariable Long surveyId,
            @PathVariable Long userId) {
        log.info("Fetching all results for surveyId: {} and userId: {}", surveyId, userId);
        List<SurveyResultDTO> results = surveyResultService.getAllSurveyResults(surveyId, userId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{surveyId}/results/{userId}/attempt/{attemptNumber}")
    public ResponseEntity<SurveyResultDTO> getResultByAttempt(
            @PathVariable Long surveyId,
            @PathVariable Long userId,
            @PathVariable Integer attemptNumber) {
        log.info("Fetching result for surveyId: {}, userId: {}, attemptNumber: {}",
                surveyId, userId, attemptNumber);
        SurveyResultDTO result = surveyResultService.getSurveyResultByAttempt(surveyId, userId, attemptNumber);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Error occurred: ", ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}