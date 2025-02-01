package com.sms.hrsam.controller;

import com.sms.hrsam.dto.CompanyDTO;
import com.sms.hrsam.dto.UserDTO;
import com.sms.hrsam.entity.Company;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.service.CompanyService;
import com.sms.hrsam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;

    @Autowired
    public CompanyController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);

        List<UserDTO> userDTOs = company.getUsers().stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .build())
                .collect(Collectors.toList());

        CompanyDTO companyDTO = CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .userCount(company.getUsers() != null ? company.getUsers().size() : 0)
                .users(userDTOs)
                .build();

        return ResponseEntity.ok(companyDTO);
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        List<CompanyDTO> companyDTOs = companies.stream()
                .map(company -> CompanyDTO.builder()
                        .id(company.getId())
                        .name(company.getName())
                        .description(company.getDescription())
                        .userCount(company.getUsers() != null ? company.getUsers().size() : 0)
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(companyDTOs);
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

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody Company company) {
        Company createdCompany = companyService.createCompany(company);
        CompanyDTO companyDTO = CompanyDTO.builder()
                .id(createdCompany.getId())
                .name(createdCompany.getName())
                .description(createdCompany.getDescription())
                .build();
        return ResponseEntity.ok(companyDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        company.setId(id);
        Company updatedCompany = companyService.updateCompany(company);
        CompanyDTO companyDTO = CompanyDTO.builder()
                .id(updatedCompany.getId())
                .name(updatedCompany.getName())
                .description(updatedCompany.getDescription())
                .build();
        return ResponseEntity.ok(companyDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok().build();
    }
}