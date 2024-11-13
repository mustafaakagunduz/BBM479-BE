package com.sms.hrsam.repository;

import com.sms.hrsam.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {

    @Query("SELECT COALESCE(MAX(sr.attemptNumber), 0) FROM SurveyResult sr " +
            "WHERE sr.survey.id = :surveyId AND sr.user.id = :userId")
    Optional<Integer> findMaxAttemptNumberBySurveyIdAndUserId(
            @Param("surveyId") Long surveyId,
            @Param("userId") Long userId
    );

    Optional<SurveyResult> findFirstBySurveyIdAndUserIdOrderByAttemptNumberDesc(
            Long surveyId,
            Long userId
    );

    List<SurveyResult> findAllBySurveyIdAndUserIdOrderByAttemptNumberDesc(
            Long surveyId,
            Long userId
    );

    Optional<SurveyResult> findBySurveyIdAndUserIdAndAttemptNumber(
            Long surveyId,
            Long userId,
            Integer attemptNumber
    );

    @Query("SELECT sr FROM SurveyResult sr " +
            "WHERE sr.survey.id = :surveyId " +
            "AND sr.user.id = :userId " +
            "AND sr.createdAt > :timestamp " +
            "ORDER BY sr.createdAt DESC")
    Optional<SurveyResult> findRecentBySurveyIdAndUserId(
            @Param("surveyId") Long surveyId,
            @Param("userId") Long userId,
            @Param("timestamp") LocalDateTime timestamp
    );

}