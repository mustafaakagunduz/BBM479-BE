package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Response;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository interfaces
@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findBySurveyId(Long surveyId);
}