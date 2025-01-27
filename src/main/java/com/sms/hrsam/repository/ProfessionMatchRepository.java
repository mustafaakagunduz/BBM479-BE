package com.sms.hrsam.repository;

import com.sms.hrsam.entity.ProfessionMatch;
import com.sms.hrsam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionMatchRepository extends JpaRepository<ProfessionMatch, Long> {
    void deleteAllBySurveyResultId(Long surveyResultId);
}