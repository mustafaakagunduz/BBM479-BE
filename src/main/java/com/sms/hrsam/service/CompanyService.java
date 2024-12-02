package com.sms.hrsam.service;

import com.sms.hrsam.entity.Company;
import com.sms.hrsam.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> searchCompanies(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllCompanies();
        }
        return companyRepository.searchByName(searchTerm);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
}