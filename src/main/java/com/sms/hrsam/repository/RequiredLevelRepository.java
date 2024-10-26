package com.sms.hrsam.repository;

import com.sms.hrsam.entity.RequiredLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface RequiredLevelRepository extends JpaRepository<RequiredLevel, Long> {
    List<RequiredLevel> findByProfessionId(Long professionId);
}