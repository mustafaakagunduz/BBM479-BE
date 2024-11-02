package com.sms.hrsam.repository;

import com.sms.hrsam.entity.RequiredLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RequiredLevelRepository extends JpaRepository<RequiredLevel, Long> {
    List<RequiredLevel> findByProfessionId(Long professionId);

    @Modifying
    @Transactional
    void deleteByProfessionId(Long professionId);
}
