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
import java.util.Optional;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class SurveyResultController {

    private final SurveyResultService surveyResultService;
    private final ResponseService responseService;

    @GetMapping("/{surveyId}/results/{resultId}")
    public ResponseEntity<SurveyResultDTO> getResultById(
            @PathVariable Long surveyId,
            @PathVariable Long resultId) {
        log.info("Fetching specific result for surveyId: {} and resultId: {}", surveyId, resultId);
        return surveyResultService.findSurveyResultById(resultId)
                .filter(result -> result.getSurveyId().equals(surveyId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{surveyId}/results/{userId}/calculate")
    public ResponseEntity<SurveyResultDTO> calculateResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId,
            @RequestParam(required = false) Long t,
            HttpServletRequest request) {

        if (t != null) {
            return ResponseEntity.ok(surveyResultService.calculateAndSaveSurveyResult(surveyId, userId));
        }

        String requestKey = "calculating_" + surveyId + "_" + userId;
        Object mutex = request.getSession().getAttribute(requestKey);

        if (mutex != null) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(null);
        }

        try {
            request.getSession().setAttribute(requestKey, true);

            // Önce anketin tamamlanıp tamamlanmadığını kontrol et
            boolean isCompleted = responseService.isSurveyCompletedByUser(surveyId, userId);
            if (!isCompleted) {
                return ResponseEntity
                        .badRequest()
                        .body(null);
            }

            // Var olan son sonucu kontrol et
            Optional<SurveyResultDTO> existingResult = surveyResultService.findLatestSurveyResult(surveyId, userId);
            if (existingResult.isPresent()) {
                LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
                if (existingResult.get().getCreatedAt().isAfter(fiveMinutesAgo)) {
                    return ResponseEntity.ok(existingResult.get());
                }
            }

            log.info("Calculating new result for surveyId: {} and userId: {}", surveyId, userId);
            SurveyResultDTO result = surveyResultService.calculateAndSaveSurveyResult(surveyId, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error calculating result:", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } finally {
            request.getSession().removeAttribute(requestKey);
        }
    }

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

    @GetMapping("/{surveyId}/results/{userId}/latest")
    public ResponseEntity<SurveyResultDTO> getLatestResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId) {
        log.info("Fetching latest result for surveyId: {} and userId: {}", surveyId, userId);
        return surveyResultService.findLatestSurveyResult(surveyId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
        return surveyResultService.findSurveyResultByAttempt(surveyId, userId, attemptNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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