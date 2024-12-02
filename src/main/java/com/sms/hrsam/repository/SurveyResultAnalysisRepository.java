package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Option;
import com.sms.hrsam.entity.SurveyResultAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyResultAnalysisRepository extends JpaRepository<SurveyResultAnalysis, Long> {
    Optional<SurveyResultAnalysis> findBySurveyResultId(Long surveyResultId);
}
