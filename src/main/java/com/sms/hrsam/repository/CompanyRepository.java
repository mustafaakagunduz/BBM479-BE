package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE LOWER(concat('%', :searchTerm, '%'))")
    List<Company> searchByName(@Param("searchTerm") String searchTerm);

    Optional<Company> findByName(String name);
}