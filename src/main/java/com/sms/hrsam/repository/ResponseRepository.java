package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findBySurveyId(Long surveyId);

    List<Response> findBySurveyIdAndUserId(Long surveyId, Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(r) FROM Response r WHERE r.user.id = :userId AND r.survey.id = :surveyId AND r.createdAt > :timestamp")
    int countRecentResponses(@Param("userId") Long userId, @Param("surveyId") Long surveyId, @Param("timestamp") LocalDateTime timestamp);

    default long countBySurveyIdAndUserId(Long surveyId, Long userId) {
        return findBySurveyIdAndUserId(surveyId, userId).size();
    }

    @Query("SELECT MAX(r.attemptNumber) FROM Response r WHERE r.user.id = :userId AND r.survey.id = :surveyId")
    Optional<Integer> findLastAttemptNumber(@Param("userId") Long userId, @Param("surveyId") Long surveyId);

    boolean existsByUserIdAndSurveyIdAndCreatedAtAfter(Long userId, Long surveyId, LocalDateTime localDateTime);
}