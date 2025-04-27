package com.sms.hrsam.controller;

import com.sms.hrsam.dto.LoginRequest;
import com.sms.hrsam.dto.RegisterRequest;
import com.sms.hrsam.dto.VerifyCodeRequest;
import com.sms.hrsam.dto.AuthResponse;
import com.sms.hrsam.dto.PasswordResetRequest;
import com.sms.hrsam.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Registration error: {}", e.getMessage());
            AuthResponse errorResponse = new AuthResponse(false, e.getMessage(), null, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Login error: {}", e.getMessage());
            AuthResponse errorResponse = new AuthResponse(false, e.getMessage(), null, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<AuthResponse> verifyCode(@RequestBody VerifyCodeRequest request) {
        try {
            AuthResponse response = authService.verifyEmail(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Email verification error: {}", e.getMessage());
            AuthResponse errorResponse = new AuthResponse(false, e.getMessage(), null, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/resend-code")
    public ResponseEntity<AuthResponse> resendVerificationCode(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(false, "Email is required", null, null)
                );
            }

            AuthResponse response = authService.resendVerificationCode(email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Resend verification code error: {}", e.getMessage());
            AuthResponse errorResponse = new AuthResponse(false, e.getMessage(), null, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(false, "Email is required", null, null)
                );
            }

            AuthResponse response = authService.initPasswordReset(email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Password reset initiation error: {}", e.getMessage());
            AuthResponse errorResponse = new AuthResponse(false, e.getMessage(), null, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<AuthResponse> resetPassword(
            @PathVariable String token,
            @RequestBody PasswordResetRequest request) {
        try {
            AuthResponse response = authService.resetPassword(token, request.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Password reset error: {}", e.getMessage());
            AuthResponse errorResponse = new AuthResponse(false, e.getMessage(), null, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/manual-verify/{userId}")
    public ResponseEntity<AuthResponse> manualVerify(@PathVariable Long userId) {
        try {
            AuthResponse response = authService.manualVerify(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Manual verification error: {}", e.getMessage());
            AuthResponse errorResponse = new AuthResponse(false, e.getMessage(), null, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}