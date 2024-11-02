package com.sms.hrsam.service;

import com.sms.hrsam.dto.ProfessionDTO;
import com.sms.hrsam.dto.RequiredSkillDTO;
import com.sms.hrsam.entity.Profession;
import com.sms.hrsam.entity.Industry;
import com.sms.hrsam.entity.RequiredLevel;
import com.sms.hrsam.entity.Skill;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.ProfessionRepository;
import com.sms.hrsam.repository.IndustryRepository;
import com.sms.hrsam.repository.RequiredLevelRepository;
import com.sms.hrsam.repository.SkillRepository;
import com.sms.hrsam.service.ProfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ProfessionServiceImpl implements ProfessionService {
    private final ProfessionRepository professionRepository;
    private final IndustryRepository industryRepository;
    private final SkillRepository skillRepository;
    private final RequiredLevelRepository requiredLevelRepository;

    @Override
    public List<ProfessionDTO> getAllProfessions() {
        return professionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProfessionDTO getProfessionById(Long id) {
        Profession profession = professionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profession not found with id: " + id));
        return convertToDTO(profession);
    }

    @Override
    @Transactional
    public ProfessionDTO createProfession(ProfessionDTO professionDTO) {
        // Industry kontrolü
        Industry industry = industryRepository.findById(professionDTO.getIndustryId())
                .orElseThrow(() -> new ResourceNotFoundException("Industry not found with id: " + professionDTO.getIndustryId()));

        // Profession oluşturma
        Profession profession = Profession.builder()
                .name(professionDTO.getName())
                .industry(industry)
                .build();

        profession = professionRepository.save(profession);

        // Required Skills ekleme
        if (professionDTO.getRequiredSkills() != null) {
            for (RequiredSkillDTO skillDTO : professionDTO.getRequiredSkills()) {
                Skill skill = skillRepository.findById(skillDTO.getSkillId())
                        .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillDTO.getSkillId()));

                RequiredLevel requiredLevel = RequiredLevel.builder()
                        .profession(profession)
                        .skill(skill)
                        .requiredLevel(skillDTO.getRequiredLevel())
                        .build();

                requiredLevelRepository.save(requiredLevel);
            }
        }

        return convertToDTO(profession);
    }

    @Override
    @Transactional
    public ProfessionDTO updateProfession(Long id, ProfessionDTO professionDTO) {
        Profession profession = professionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profession not found with id: " + id));

        Industry industry = industryRepository.findById(professionDTO.getIndustryId())
                .orElseThrow(() -> new ResourceNotFoundException("Industry not found with id: " + professionDTO.getIndustryId()));

        // Temel bilgileri güncelle
        profession.setName(professionDTO.getName());
        profession.setIndustry(industry);

        // Mevcut required levels'ları sil
        requiredLevelRepository.deleteByProfessionId(id);

        // Yeni required levels'ları ekle
        if (professionDTO.getRequiredSkills() != null) {
            for (RequiredSkillDTO skillDTO : professionDTO.getRequiredSkills()) {
                Skill skill = skillRepository.findById(skillDTO.getSkillId())
                        .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillDTO.getSkillId()));

                RequiredLevel requiredLevel = RequiredLevel.builder()
                        .profession(profession)
                        .skill(skill)
                        .requiredLevel(skillDTO.getRequiredLevel())
                        .build();

                requiredLevelRepository.save(requiredLevel);
            }
        }

        profession = professionRepository.save(profession);
        return convertToDTO(profession);
    }

    @Override
    @Transactional
    public void deleteProfession(Long id) {
        if (!professionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profession not found with id: " + id);
        }
        // RequiredLevel'lar cascade ile silinecek
        professionRepository.deleteById(id);
    }

    private ProfessionDTO convertToDTO(Profession profession) {
        // Required Skills'leri getir
        List<RequiredLevel> requiredLevels = requiredLevelRepository.findByProfessionId(profession.getId());

        List<RequiredSkillDTO> requiredSkillDTOs = requiredLevels.stream()
                .map(rl -> RequiredSkillDTO.builder()
                        .id(rl.getId())
                        .skillId(rl.getSkill().getId())
                        .skillName(rl.getSkill().getName())
                        .requiredLevel(rl.getRequiredLevel())
                        .build())
                .collect(Collectors.toList());

        return ProfessionDTO.builder()
                .id(profession.getId())
                .name(profession.getName())
                .industryId(profession.getIndustry().getId())
                .industryName(profession.getIndustry().getName())
                .requiredSkills(requiredSkillDTOs)
                .build();
    }
}
