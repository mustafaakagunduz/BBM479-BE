package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    /*@Query("SELECT r FROM Response r WHERE r.survey.id = :surveyId AND r.user.id = :userId")
    List<Response> findBySurveyIdAndUserId(@Param("surveyId") Long surveyId, @Param("userId") Long userId);*/


    List<Response> findBySurveyId(Long surveyId);

    default long countBySurveyIdAndUserId(Long surveyId, Long userId) {
        return findBySurveyIdAndUserId(surveyId, userId).size();
    }

    List<Response> findBySurveyIdAndUserId(Long surveyId, Long userId);
}
