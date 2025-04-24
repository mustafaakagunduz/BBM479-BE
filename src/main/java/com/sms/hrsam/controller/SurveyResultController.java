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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@Slf4j
public class SurveyResultController {

    private final SurveyResultService surveyResultService;
    private final ResponseService responseService;
    private static final Map<String, Object> locks = new ConcurrentHashMap<>();
    private static final Map<String, LocalDateTime> lastRequestTimes = new ConcurrentHashMap<>();

    @GetMapping("/{surveyId}/results/{userId}/latest")
    public ResponseEntity<SurveyResultDTO> getLatestResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId) {
        String key = String.format("result_%d_%d", surveyId, userId);

        // Son istek zamanını kontrol et
        LocalDateTime lastRequestTime = lastRequestTimes.get(key);
        if (lastRequestTime != null &&
                lastRequestTime.plusSeconds(1).isAfter(LocalDateTime.now())) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .build();
        }

        // İstek zamanını güncelle
        lastRequestTimes.put(key, LocalDateTime.now());

        try {
            log.info("Fetching latest result for surveyId: {} and userId: {}", surveyId, userId);
            return surveyResultService.findLatestSurveyResult(surveyId, userId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } finally {
            // Cache temizliği
            cleanupCache();
        }
    }

    private void cleanupCache() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
        lastRequestTimes.entrySet().removeIf(entry ->
                entry.getValue().isBefore(threshold));
    }
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
    private static final Map<String, Lock> calculationLocks = new ConcurrentHashMap<>();

    @PostMapping("/{surveyId}/results/{userId}/calculate")
    public ResponseEntity<SurveyResultDTO> calculateResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean force,
            HttpServletRequest request) {

        try {
            // Önce anketin tamamlanıp tamamlanmadığını kontrol et
            boolean isCompleted = responseService.isSurveyCompletedByUser(surveyId, userId);
            if (!isCompleted) {
                return ResponseEntity
                        .badRequest()
                        .body(null);
            }

            // Calculate the result with the force parameter
            SurveyResultDTO result = surveyResultService.calculateAndSaveSurveyResult(surveyId, userId, force);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Error calculating result:", e);

            // Only return rate limit response for specific rate limit errors
            if (e.getMessage().contains("in progress") || e.getMessage().contains("taking longer")) {
                return ResponseEntity
                        .status(HttpStatus.TOO_MANY_REQUESTS)
                        .header("Retry-After", "5")
                        .body(null);
            }

            // For other errors, return a more general error
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    private void cleanupLocks(String currentKey) {
        // 1000'den fazla kilit birikirse en eskilerini temizle
        if (calculationLocks.size() > 1000) {
            calculationLocks.remove(currentKey);
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
    @GetMapping("/results/user/{userId}")
    public ResponseEntity<List<SurveyResultDTO>> getAllResultsByUserId(
            @PathVariable Long userId) {
        log.info("Fetching all results for userId: {}", userId);
        List<SurveyResultDTO> results = surveyResultService.getAllResultsByUserId(userId);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/results/{resultId}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long resultId) {
        log.info("Deleting result with id: {}", resultId);
        surveyResultService.deleteResult(resultId);
        return ResponseEntity.noContent().build();
    }
}