package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Survey;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository interfaces
@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByUserId(Long userId);
}