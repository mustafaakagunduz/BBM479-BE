// SkillRepository.java
package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByIndustryId(Long industryId);
    boolean existsByNameAndIndustryId(String name, Long industryId);
}