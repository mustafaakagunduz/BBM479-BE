package com.sms.hrsam.service;

import com.sms.hrsam.entity.Company;
import com.sms.hrsam.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
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

    @Transactional
    public Company createCompany(Company company) {
        // Şirket adının benzersiz olduğunu kontrol et
        if (companyRepository.findByName(company.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name already exists");
        }
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(Company company) {
        // Şirketin var olduğunu kontrol et
        Company existingCompany = getCompanyById(company.getId());

        // Eğer isim değiştiyse ve yeni isim başka bir şirkette kullanılıyorsa hata ver
        if (!existingCompany.getName().equals(company.getName()) &&
                companyRepository.findByName(company.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name already exists");
        }

        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);
        // Şirkette çalışan varsa silmeyi engelle
        if (company.getUsers() != null && !company.getUsers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot delete company with active employees");
        }
        companyRepository.deleteById(id);
    }
}