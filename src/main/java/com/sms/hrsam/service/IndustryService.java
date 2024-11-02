// IndustryService.java
package com.sms.hrsam.service;

import com.sms.hrsam.dto.IndustryDTO;
import com.sms.hrsam.entity.Industry;
import com.sms.hrsam.repository.IndustryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndustryService {

    private final IndustryRepository industryRepository;

    public List<IndustryDTO> getAllIndustries() {
        return industryRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public IndustryDTO getIndustryById(Long id) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Industry not found"));
        return convertToDTO(industry);
    }

    @Transactional
    public IndustryDTO createIndustry(IndustryDTO industryDTO) {
        if (industryRepository.existsByName(industryDTO.getName())) {
            throw new RuntimeException("Industry with this name already exists");
        }

        Industry industry = Industry.builder()
                .name(industryDTO.getName())
                .build();

        Industry savedIndustry = industryRepository.save(industry);
        return convertToDTO(savedIndustry);
    }

    @Transactional
    public IndustryDTO updateIndustry(Long id, IndustryDTO industryDTO) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        industry.setName(industryDTO.getName());
        Industry updatedIndustry = industryRepository.save(industry);
        return convertToDTO(updatedIndustry);
    }

    @Transactional
    public void deleteIndustry(Long id) {
        if (!industryRepository.existsById(id)) {
            throw new RuntimeException("Industry not found");
        }
        industryRepository.deleteById(id);
    }

    private IndustryDTO convertToDTO(Industry industry) {
        return IndustryDTO.builder()
                .id(industry.getId())
                .name(industry.getName())
                .build();
    }
}