package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Skill;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interfaces
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}