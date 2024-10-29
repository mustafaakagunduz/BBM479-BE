package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {

    // Belirli bir soruya ait seçenekleri getir
    List<Option> findByQuestionId(Long questionId);
}
