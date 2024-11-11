package com.sms.hrsam.repository;

import com.sms.hrsam.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {

    @Query("SELECT sr FROM SurveyResult sr " +
            "LEFT JOIN FETCH sr.professionMatches pm " +
            "LEFT JOIN FETCH pm.profession " +
            "WHERE sr.survey.id = :surveyId AND sr.user.id = :userId")
    List<SurveyResult> findAllBySurveyIdAndUserId(
            @Param("surveyId") Long surveyId,
            @Param("userId") Long userId
    );
}