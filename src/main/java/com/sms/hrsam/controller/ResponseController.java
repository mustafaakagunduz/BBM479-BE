package com.sms.hrsam.controller;

import com.sms.hrsam.dto.SurveyResponseCreateDTO;
import com.sms.hrsam.entity.Response;
import com.sms.hrsam.service.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/responses")
@Slf4j
public class ResponseController {

    private final ResponseService responseService;

    @Autowired
    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<Void> createResponses(
            @RequestBody SurveyResponseCreateDTO responseDTO,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey)
    {
        responseService.createResponses(responseDTO, idempotencyKey);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<Response>> getResponsesBySurveyId(@PathVariable Long surveyId) {
        return ResponseEntity.ok(responseService.getResponsesBySurveyId(surveyId));
    }

    @GetMapping("/{surveyId}/responses/check/{userId}")
    public ResponseEntity<Map<String, Object>> checkSurveyCompletion(
            @PathVariable Long surveyId,
            @PathVariable Long userId) {
        log.info("Checking survey completion for surveyId: {} and userId: {}", surveyId, userId);

        boolean isCompleted = responseService.isSurveyCompletedByUser(surveyId, userId);
        log.info("Survey completion status: {}", isCompleted);

        Map<String, Object> response = new HashMap<>();
        response.put("completed", isCompleted);

        return ResponseEntity.ok(response);
    }
}