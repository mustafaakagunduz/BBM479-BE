package com.sms.hrsam.service;

import com.sms.hrsam.entity.Skill;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }



    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
}
