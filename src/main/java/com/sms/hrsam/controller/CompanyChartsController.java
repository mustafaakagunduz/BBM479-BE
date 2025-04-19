package com.sms.hrsam.controller;

import com.sms.hrsam.dto.CompanySkillStatsDTO;
import com.sms.hrsam.service.CompanyChartsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company-charts")
@RequiredArgsConstructor
public class CompanyChartsController {
    private static final Logger log = LoggerFactory.getLogger(CompanyChartsController.class);

    private final CompanyChartsService companyChartsService;

    @GetMapping("/{companyId}/skills/stats")
    public ResponseEntity<List<CompanySkillStatsDTO>> getCompanySkillStats(@PathVariable Long companyId) {
        log.info("Fetching stats for company: {}", companyId);
        List<CompanySkillStatsDTO> stats = companyChartsService.getAllSkillsStatsForCompany(companyId);
        log.info("Found {} stats", stats.size());
        return ResponseEntity.ok(stats);
    }
}
