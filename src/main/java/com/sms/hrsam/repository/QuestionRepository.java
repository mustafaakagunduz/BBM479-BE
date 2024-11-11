package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Belirli bir anketin sorularını getir
    List<Question> findBySurveyId(Long surveyId);

    default long countBySurveyId(Long surveyId) {
        return findBySurveyId(surveyId).size();
    }



}
