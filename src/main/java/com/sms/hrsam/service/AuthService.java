package com.sms.hrsam.service;

import com.sms.hrsam.dto.LoginRequest;
import com.sms.hrsam.dto.RegisterRequest;
import com.sms.hrsam.dto.AuthResponse;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.entity.Role;
import com.sms.hrsam.entity.UserRole;
import com.sms.hrsam.repository.UserRepository;
import com.sms.hrsam.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setName(request.getFirstName() + " " + request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set default role as USER
        Role userRole = roleRepository.findByName(UserRole.USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(userRole);

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusDays(1));
        user.setEmailVerified(false);

        userRepository.save(user);

        // Send verification email
        try {
            emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        } catch (Exception e) {
            // Log the error but don't prevent registration
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
        return new AuthResponse(
                true,
                "User registered successfully. Please check your email for verification.",
                user.getRole().getNameAsString(),
                user.getId()
        );
    }

    public AuthResponse login(LoginRequest request) {
        try {
            System.out.println("Login attempt for email: " + request.getEmail());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            System.out.println("User details:");
            System.out.println("- ID: " + user.getId());
            System.out.println("- Email: " + user.getEmail());
            System.out.println("- EmailVerified: " + user.isEmailVerified());
            System.out.println("- Role: " + (user.getRole() != null ? user.getRole().getName() : "null"));
            System.out.println("- Role ID: " + (user.getRole() != null ? user.getRole().getId() : "null"));

            if (!user.isEmailVerified()) {
                System.out.println("Email not verified for user: " + user.getEmail());
                throw new RuntimeException("Please verify your email before logging in.");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                System.out.println("Invalid password for user: " + user.getEmail());
                throw new RuntimeException("Invalid password");
            }

            // Role null check
            if (user.getRole() == null) {
                System.err.println("Role is null for user: " + user.getEmail());
                throw new RuntimeException("User role not found");
            }

            UserRole roleName = user.getRole().getName();
            System.out.println("Role name enum value: " + roleName);
            String roleNameString = roleName.name();
            System.out.println("Role name string: " + roleNameString);

            AuthResponse response = new AuthResponse(
                    true,
                    "Login successful",
                    roleNameString,
                    user.getId()
            );

            System.out.println("Auth response created: " + response);
            return response;
        } catch (Exception e) {
            System.err.println("Login error for " + request.getEmail());
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getName());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public AuthResponse manualVerify(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);

        return new AuthResponse(
                true,
                "Email verified successfully",
                user.getRole().getNameAsString(),
                user.getId()
        );
    }

    public AuthResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);

        return new AuthResponse(
                true,
                "Email verified successfully",
                user.getRole().getNameAsString(),
                user.getId()
        );
    }
}