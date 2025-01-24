package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyChartsRepository extends JpaRepository<Response, Long> {
    @Query("SELECT DISTINCT q.skill.id FROM Response r " +
            "JOIN r.question q " +
            "JOIN r.user u " +
            "WHERE u.company.id = :companyId")
    List<Long> findDistinctSkillIdsByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT r FROM Response r " +
            "JOIN r.user u " +
            "JOIN r.question q " +
            "JOIN q.skill s " +
            "WHERE u.company.id = :companyId " +
            "AND s.id = :skillId")
    List<Response> findAllResponsesByCompanyAndSkill(
            @Param("companyId") Long companyId,
            @Param("skillId") Long skillId
    );
}