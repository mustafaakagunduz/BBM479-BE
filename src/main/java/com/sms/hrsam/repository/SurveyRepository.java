package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    // Belirli bir kullanıcıya ait anketleri getir
    List<Survey> findByUserId(Long userId);


}
