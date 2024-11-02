// SkillService.java
package com.sms.hrsam.service;

import com.sms.hrsam.dto.SkillDTO;
import com.sms.hrsam.entity.Industry;
import com.sms.hrsam.entity.Skill;
import com.sms.hrsam.repository.IndustryRepository;
import com.sms.hrsam.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final IndustryRepository industryRepository;

    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SkillDTO> getSkillsByIndustry(Long industryId) {
        return skillRepository.findByIndustryId(industryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SkillDTO createSkill(SkillDTO skillDTO) {
        Industry industry = industryRepository.findById(skillDTO.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        if (skillRepository.existsByNameAndIndustryId(skillDTO.getName(), skillDTO.getIndustryId())) {
            throw new RuntimeException("Skill already exists in this industry");
        }

        Skill skill = Skill.builder()
                .name(skillDTO.getName())
                .industry(industry)
                .build();

        Skill savedSkill = skillRepository.save(skill);
        return convertToDTO(savedSkill);
    }

    @Transactional
    public SkillDTO updateSkill(Long id, SkillDTO skillDTO) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        Industry industry = industryRepository.findById(skillDTO.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        skill.setName(skillDTO.getName());
        skill.setIndustry(industry);

        Skill updatedSkill = skillRepository.save(skill);
        return convertToDTO(updatedSkill);
    }

    @Transactional
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new RuntimeException("Skill not found");
        }
        skillRepository.deleteById(id);
    }

    private SkillDTO convertToDTO(Skill skill) {
        return SkillDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .industryId(skill.getIndustry().getId())
                .industryName(skill.getIndustry().getName())
                .build();
    }
}