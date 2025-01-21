package com.sms.hrsam.controller;

import com.sms.hrsam.dto.CompanyDTO;
import com.sms.hrsam.entity.Company;
import com.sms.hrsam.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*") // Geliştirme aşamasında CORS için
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam(required = false) String term) {
        List<Company> companies = companyService.searchCompanies(term);
        return ResponseEntity.ok(companies);
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Company API is working!");
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        List<CompanyDTO> companyDTOs = companies.stream()
                .map(company -> new CompanyDTO(company.getId(), company.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(companyDTOs);
    }
}