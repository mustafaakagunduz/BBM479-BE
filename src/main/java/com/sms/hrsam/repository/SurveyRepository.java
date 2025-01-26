package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    // Belirli bir kullanıcıya ait anketleri getir
    List<Survey> findByUserId(Long userId);

    @Query("SELECT s FROM Survey s JOIN FETCH s.questions q JOIN FETCH q.skill WHERE s.id = :surveyId")
    Optional<Survey> findByIdWithQuestionsAndSkills(@Param("surveyId") Long surveyId);

}
