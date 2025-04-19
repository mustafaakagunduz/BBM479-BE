package com.sms.hrsam.service;

import com.sms.hrsam.dto.LoginRequest;
import com.sms.hrsam.dto.RegisterRequest;
import com.sms.hrsam.dto.AuthResponse;
import com.sms.hrsam.entity.Company;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.entity.Role;
import com.sms.hrsam.entity.UserRole;
import com.sms.hrsam.repository.CompanyRepository;
import com.sms.hrsam.repository.UserRepository;
import com.sms.hrsam.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CompanyRepository companyRepository;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public AuthResponse register(RegisterRequest request) {
        // Email check
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new RuntimeException("This email is already registered");
        }

        try {
            // Company check
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));

            // Create new user
            User user = new User();
            user.setName(request.getFirstName() + " " + request.getLastName());
            user.setEmail(request.getEmail().toLowerCase().trim());
            user.setUsername(request.getEmail().toLowerCase().trim());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCompany(company);

            // Set emailVerified to true - skip email verification
            user.setEmailVerified(true);

            // Check if this is the first user (will have ID 1)
            boolean isFirstUser = userRepository.count() == 0;

            // Assign ADMIN role for first user, USER role for others
            UserRole roleType = isFirstUser ? UserRole.ADMIN : UserRole.USER;
            Role role = roleRepository.findByName(roleType)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleType));
            user.setRole(role);

            User savedUser = userRepository.save(user);

            log.info("User registered with role {}: {}", roleType, user.getEmail());
            log.info("User registered successfully: {}", user.getEmail());

            return new AuthResponse(
                    true,
                    "Registration successful. You can now log in.",
                    user.getRole().getName().toString(),
                    savedUser.getId()
            );

        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage());
            throw new RuntimeException("An error occurred during registration: " + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            log.info("Login attempt for email: {}", request.getEmail());

            String normalizedEmail = request.getEmail().toLowerCase().trim();
            User user = userRepository.findByEmail(normalizedEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // No need to check if email is verified since all users are now verified by default
            // This check is removed: if (!user.isEmailVerified()) { ... }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                log.warn("Invalid password for user: {}", user.getEmail());
                throw new RuntimeException("Invalid password");
            }

            if (user.getRole() == null) {
                log.error("Role is null for user: {}", user.getEmail());
                throw new RuntimeException("User role not found");
            }

            // Create UserDTO with all necessary information
            AuthResponse.UserDTO userDTO = new AuthResponse.UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setUsername(user.getUsername());

            // Set role
            AuthResponse.RoleDTO roleDTO = new AuthResponse.RoleDTO();
            roleDTO.setName(user.getRole().getName().toString());
            userDTO.setRole(roleDTO);

            // Set email verified status (always true now)
            userDTO.setEmailVerified(true);

            // Create AuthResponse
            AuthResponse response = new AuthResponse();
            response.setSuccess(true);
            response.setMessage("Login successful");
            response.setUser(userDTO);

            log.info("Login successful for email: {}", user.getEmail());
            return response;

        } catch (Exception e) {
            log.error("Login error for {}: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }

    @Transactional
    public AuthResponse initPasswordReset(String email) {
        try {
            User user = userRepository.findByEmail(email.toLowerCase().trim())
                    .orElseThrow(() -> new RuntimeException("No user found with this email address"));

            // Create password reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetPasswordToken(resetToken);
            user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            // Send password reset email
            try {
                emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
                log.info("Password reset email sent to: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send password reset email: {}", e.getMessage());
                throw new RuntimeException("Failed to send password reset email");
            }

            return new AuthResponse(
                    true,
                    "Password reset link has been sent to your email",
                    null,
                    user.getId()
            );

        } catch (Exception e) {
            log.error("Password reset initiation failed for email {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to initiate password reset: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponse resetPassword(String token, String newPassword) {
        try {
            User user = userRepository.findByResetPasswordToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid password reset link"));

            // Check token expiry
            if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Password reset link has expired");
            }

            // Validate new password
            if (newPassword == null || newPassword.trim().length() < 6) {
                throw new RuntimeException("Password must be at least 6 characters");
            }

            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiry(null);
            userRepository.save(user);

            log.info("Password reset successful for user: {}", user.getEmail());

            return new AuthResponse(
                    true,
                    "Your password has been updated successfully",
                    user.getRole().getName().toString(),
                    user.getId()
            );

        } catch (Exception e) {
            log.error("Password reset failed for token {}: {}", token, e.getMessage());
            throw new RuntimeException("Password reset failed: " + e.getMessage());
        }
    }
    // AuthService.java dosyasına eklenecek metodlar

    @Transactional
    public AuthResponse manualVerify(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Since we're skipping email verification, just ensure it's set to true
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            user.setVerificationTokenExpiry(null);
            userRepository.save(user);

            log.info("Manual verification completed for user ID: {}", userId);

            return new AuthResponse(
                    true,
                    "Email verification successful",
                    user.getRole().getName().toString(),
                    user.getId()
            );
        } catch (Exception e) {
            log.error("Manual verification error for userId {}: {}", userId, e.getMessage());
            throw new RuntimeException("An error occurred during manual verification: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponse resendVerificationEmail(String email) {
        try {
            User user = userRepository.findByEmail(email.toLowerCase().trim())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Since we're skipping email verification, just mark as verified
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            user.setVerificationTokenExpiry(null);
            userRepository.save(user);

            return new AuthResponse(
                    true,
                    "Email is now verified",
                    user.getRole().getName().toString(),
                    user.getId()
            );
        } catch (Exception e) {
            log.error("Error handling verification email request: {}", e.getMessage());
            throw new RuntimeException("An error occurred while processing your request");
        }
    }
}