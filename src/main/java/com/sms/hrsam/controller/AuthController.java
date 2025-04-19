package com.sms.hrsam.controller;

import com.sms.hrsam.dto.LoginRequest;
import com.sms.hrsam.dto.RegisterRequest;
import com.sms.hrsam.dto.AuthResponse;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.repository.UserRepository;
import com.sms.hrsam.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService ) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("Register request received for email: {}", request.getEmail());
        try {
            AuthResponse response = authService.register(request);
            log.info("Registration successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Registration failed for email: {}: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        try {
            AuthResponse response = authService.login(request);
            log.info("Login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for email {}: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }
    @Transactional
    public AuthResponse verifyEmail(String token, UserRepository userRepository) {
        log.info("Starting verification for token: {}", token);
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        log.info("Found user: {}, verified: {}", user.getEmail(), user.isEmailVerified());

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        log.info("User verified: {}", user.getEmail());

        return new AuthResponse(true, "Email verified", user.getRole().getName().toString(), user.getId());
    }
    @PostMapping("/verify-manual/{userId}")
    public ResponseEntity<AuthResponse> manualVerify(@PathVariable Long userId) {
        log.info("Manual verification request received for userId: {}", userId);
        try {
            AuthResponse response = authService.manualVerify(userId);
            log.info("Manual verification successful for userId: {}", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Manual verification failed for userId {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<AuthResponse> resendVerification(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("Resend verification email request received for: {}", email);
        try {
            AuthResponse response = authService.resendVerificationEmail(email);
            log.info("Verification email resent successfully to: {}", email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to resend verification email to {}: {}", email, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("Password reset request received for: {}", email);
        try {
            AuthResponse response = authService.initPasswordReset(email);
            log.info("Password reset initiated for: {}", email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to initiate password reset for {}: {}", email, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<AuthResponse> resetPassword(
            @PathVariable String token,
            @RequestBody Map<String, String> request) {
        String newPassword = request.get("password");
        log.info("Password reset attempt for token: {}", token);
        try {
            AuthResponse response = authService.resetPassword(token, newPassword);
            log.info("Password reset successful for token: {}", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Password reset failed for token {}: {}", token, e.getMessage());
            throw e;
        }
    }
}