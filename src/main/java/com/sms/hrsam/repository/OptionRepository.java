package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interfaces
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}