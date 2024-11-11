package com.sms.hrsam.repository;

import com.sms.hrsam.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {
    Optional<SurveyResult> findBySurveyIdAndUserId(Long surveyId, Long userId);
}