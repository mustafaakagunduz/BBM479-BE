// IndustryRepository.java
package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndustryRepository extends JpaRepository<Industry, Long> {
    boolean existsByName(String name);
}
