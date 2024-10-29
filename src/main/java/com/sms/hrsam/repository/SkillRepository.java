package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    // No need to declare these methods as JpaRepository provides them
    // public Skill createSkill(Skill skill); // This is unnecessary
    // public List<Skill> getAllSkills(); // This is unnecessary
}
