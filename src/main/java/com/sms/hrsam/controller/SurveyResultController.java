// SurveyResultController.java
package com.sms.hrsam.controller;

import com.sms.hrsam.dto.SurveyResultDTO;
import com.sms.hrsam.service.SurveyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
@CrossOrigin(origins = "*")
public class SurveyResultController {
    private final SurveyResultService surveyResultService;

    @Autowired
    public SurveyResultController(SurveyResultService surveyResultService) {
        this.surveyResultService = surveyResultService;
    }

    @GetMapping("/{surveyId}/results/{userId}")
    public ResponseEntity<SurveyResultDTO> getSurveyResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(surveyResultService.getSurveyResult(surveyId, userId));
    }

    @PostMapping("/{surveyId}/results/{userId}/calculate")
    public ResponseEntity<SurveyResultDTO> calculateSurveyResult(
            @PathVariable Long surveyId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(surveyResultService.calculateAndSaveSurveyResult(surveyId, userId));
    }
}