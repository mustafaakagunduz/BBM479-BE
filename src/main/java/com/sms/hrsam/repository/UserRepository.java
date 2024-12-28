package com.sms.hrsam.repository;

import com.sms.hrsam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndEmail(String username, String email);

    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    // Yeni eklenen metodlar
    Optional<User> findByResetPasswordToken(String token);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.resetPasswordToken = :token")
    Optional<User> findByResetPasswordTokenWithRole(String token);

    boolean existsByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.emailVerified = true")
    boolean existsByEmailAndVerified(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.id <> :userId")
    boolean existsByEmailExceptUser(String email, Long userId);
}