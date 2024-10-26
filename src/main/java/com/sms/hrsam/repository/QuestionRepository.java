package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Question;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interfaces
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}