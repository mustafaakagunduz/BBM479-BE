package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interfaces
@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
}