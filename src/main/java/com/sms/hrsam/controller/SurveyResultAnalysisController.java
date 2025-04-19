package com.sms.hrsam.controller;

import com.sms.hrsam.dto.ProfessionDTO;
import com.sms.hrsam.entity.SurveyAnalysisRequest;
import com.sms.hrsam.entity.SurveyResultAnalysis;
import com.sms.hrsam.service.ProfessionService;
import com.sms.hrsam.service.SurveyResultAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/surveys")
public class SurveyResultAnalysisController {
    private final SurveyResultAnalysisService analysisService;

    @Autowired
    public SurveyResultAnalysisController(SurveyResultAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/{surveyId}/results/{resultId}/analysis")
    public ResponseEntity<SurveyResultAnalysis> createOrUpdateAnalysis(
            @PathVariable Long surveyId,
            @PathVariable Long resultId,
            @RequestBody SurveyAnalysisRequest request
    ) {
        SurveyResultAnalysis analysis = analysisService.saveAnalysis(resultId, request);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/results/{resultId}/analysis")
    public ResponseEntity<SurveyResultAnalysis> getAnalysis(@PathVariable Long resultId) {
        SurveyResultAnalysis analysis = analysisService.getAnalysis(resultId);
        return ResponseEntity.ok(analysis);
    }
}