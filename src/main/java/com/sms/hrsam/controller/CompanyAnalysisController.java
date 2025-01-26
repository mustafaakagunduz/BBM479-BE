package com.sms.hrsam.controller;

import com.sms.hrsam.dto.CompanySkillAnalysisDTO;
import com.sms.hrsam.service.CompanyAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "http://localhost:3000")
public class CompanyAnalysisController {
    private final CompanyAnalysisService companyAnalysisService;

    @Autowired
    public CompanyAnalysisController(CompanyAnalysisService companyAnalysisService) {
        this.companyAnalysisService = companyAnalysisService;
    }

    @GetMapping("/company/{companyId}/survey/{surveyId}/skills")
    public ResponseEntity<CompanySkillAnalysisDTO> getCompanySkillAnalysis(
            @PathVariable Long companyId,
            @PathVariable Long surveyId) {
        return ResponseEntity.ok(companyAnalysisService.analyzeCompanySkills(companyId, surveyId));
    }
}