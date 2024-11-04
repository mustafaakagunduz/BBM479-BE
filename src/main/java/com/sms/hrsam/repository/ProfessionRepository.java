package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    List<Profession> findByIndustryId(Long industryId);
    boolean existsByName(String name);
}