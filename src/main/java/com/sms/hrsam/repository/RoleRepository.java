package com.sms.hrsam.repository;

import com.sms.hrsam.entity.Role;
import com.sms.hrsam.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
    boolean existsByName(String name);
}