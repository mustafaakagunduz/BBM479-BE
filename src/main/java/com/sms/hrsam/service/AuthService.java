package com.sms.hrsam.service;

import com.sms.hrsam.dto.LoginRequest;
import com.sms.hrsam.dto.RegisterRequest;
import com.sms.hrsam.dto.AuthResponse;
import com.sms.hrsam.dto.UserDTO;
import com.sms.hrsam.entity.Company;
import com.sms.hrsam.entity.User;
import com.sms.hrsam.entity.Role;
import com.sms.hrsam.entity.UserRole;
import com.sms.hrsam.repository.CompanyRepository;
import com.sms.hrsam.repository.UserRepository;
import com.sms.hrsam.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;  // Bu import önemli
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
    private final CompanyRepository companyRepository;  // CompanyRepository'i ekleyin


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
    @Transactional
    public AuthResponse manualVerify(Long userId) {
        try {
            User user = userRepository.findById(userId)  // instance'ı kullanıyoruz
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            user.setEmailVerified(true);
            user.setVerificationToken(null);
            user.setVerificationTokenExpiry(null);
            userRepository.save(user);  // instance'ı kullanıyoruz

            log.info("Manual verification completed for user: {}", user.getEmail());

            return new AuthResponse(
                    true,
                    "Email doğrulama başarılı",
                    user.getRole().getName().toString(),
                    user.getId()
            );
        } catch (Exception e) {
            log.error("Manual verification error for userId {}: {}", userId, e.getMessage());
            throw new RuntimeException("Manuel doğrulama sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    public AuthResponse register(RegisterRequest request) {
        // Email kontrolü
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new RuntimeException("Bu email adresi zaten kayıtlı");
        }

        try {
            // Şirket kontrolü
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Şirket bulunamadı"));

            // Yeni kullanıcı oluştur
            User user = new User();
            user.setName(request.getFirstName() + " " + request.getLastName());
            user.setEmail(request.getEmail().toLowerCase().trim());
            user.setUsername(request.getEmail().toLowerCase().trim());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCompany(company);  // Şirket ilişkisini kur
            user.setEmailVerified(isAutoVerifiableEmail(request.getEmail()));

            // Varsayılan rol ata
            Role userRole = roleRepository.findByName(UserRole.USER)
                    .orElseThrow(() -> new RuntimeException("Varsayılan rol bulunamadı"));
            user.setRole(userRole);

            // Doğrulama token'ı oluştur
            String verificationToken = UUID.randomUUID().toString();
            user.setVerificationToken(verificationToken);
            user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
            user.setEmailVerified(false);  // Başlangıçta false olmalı

            User savedUser = userRepository.save(user);

            // Doğrulama emaili gönder
            try {
                emailService.sendVerificationEmail(user.getEmail(), verificationToken);
                log.info("Verification email sent to: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send verification email: {}", e.getMessage());
            }

            return new AuthResponse(
                    true,
                    "Kayıt başarılı. Lütfen email adresinizi doğrulayın.",
                    user.getRole().getName().toString(),
                    savedUser.getId()
            );

        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage());
            throw new RuntimeException("Kayıt işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    private boolean isAutoVerifiableEmail(String email) {
        // Implement your auto-verification logic
        // For example:
        return email.endsWith("@yourcompany.com") ||
                email.endsWith("@gmail.com");
    }
    public AuthResponse login(LoginRequest request) {
        try {
            log.info("Login attempt for email: {}", request.getEmail());

            String normalizedEmail = request.getEmail().toLowerCase().trim();
            User user = userRepository.findByEmail(normalizedEmail)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            if (!user.isEmailVerified()) {
                log.warn("Email not verified for user: {}", user.getEmail());
                throw new RuntimeException("Lütfen email adresinizi doğrulayın");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                log.warn("Invalid password for user: {}", user.getEmail());
                throw new RuntimeException("Geçersiz şifre");
            }

            if (user.getRole() == null) {
                log.error("Role is null for user: {}", user.getEmail());
                throw new RuntimeException("Kullanıcı rolü bulunamadı");
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

            // Set email verified status
            userDTO.setEmailVerified(user.isEmailVerified());

            // Create AuthResponse
            AuthResponse response = new AuthResponse();
            response.setSuccess(true);
            response.setMessage("Giriş başarılı");
            response.setUser(userDTO);

            log.info("Login successful for email: {}", user.getEmail());
            return response;

        } catch (Exception e) {
            log.error("Login error for {}: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }
    @Transactional
    public AuthResponse verifyEmail(String token) {
        log.info("Starting email verification for token: {}", token);

        try {
            User user = userRepository.findByVerificationToken(token)
                    .orElseThrow(() -> {
                        log.error("No user found with token: {}", token);
                        return new RuntimeException("Geçersiz doğrulama kodu");
                    });

            // Debug logları ekleyelim
            log.debug("Found user: {}", user.getEmail());
            log.debug("Current verification status: {}", user.isEmailVerified());
            log.debug("Token expiry: {}", user.getVerificationTokenExpiry());
            log.debug("Current time: {}", LocalDateTime.now());

            if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
                log.error("Token expired. Expiry: {}, Current time: {}",
                        user.getVerificationTokenExpiry(), LocalDateTime.now());
                throw new RuntimeException("Doğrulama kodunun süresi dolmuş");
            }

            user.setEmailVerified(true);
            user.setVerificationToken(null);
            user.setVerificationTokenExpiry(null);

            User savedUser = userRepository.save(user);
            log.info("Successfully verified email for user: {}", savedUser.getEmail());

            return new AuthResponse(
                    true,
                    "Email doğrulama başarılı",
                    user.getRole().getName().toString(),
                    user.getId()
            );
        } catch (Exception e) {
            log.error("Verification failed: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public AuthResponse resendVerificationEmail(String email) {
        try {
            User user = userRepository.findByEmail(email.toLowerCase().trim())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            if (user.isEmailVerified()) {
                return new AuthResponse(true, "Email zaten doğrulanmış", null, user.getId());
            }

            // Yeni doğrulama token'ı oluştur
            String newToken = UUID.randomUUID().toString();
            user.setVerificationToken(newToken);
            user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
            userRepository.save(user);

            // Yeni doğrulama emaili gönder
            emailService.sendVerificationEmail(user.getEmail(), newToken);

            return new AuthResponse(
                    true,
                    "Email doğrulama başarılı",
                    user.getRole().getName().toString(),
                    user.getId()
            );
// Modify the response construction to include emailVerified if needed
        } catch (Exception e) {
            log.error("Error resending verification email: {}", e.getMessage());
            throw new RuntimeException("Doğrulama emaili gönderilirken bir hata oluştu");
        }
    }

    @Transactional
    public AuthResponse initPasswordReset(String email) {
        try {
            User user = userRepository.findByEmail(email.toLowerCase().trim())
                    .orElseThrow(() -> new RuntimeException("Bu email adresi ile kayıtlı kullanıcı bulunamadı"));

            // Şifre sıfırlama token'ı oluştur
            String resetToken = UUID.randomUUID().toString();
            user.setResetPasswordToken(resetToken);
            user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            // Şifre sıfırlama emaili gönder
            try {
                emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
                log.info("Password reset email sent to: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send password reset email: {}", e.getMessage());
                throw new RuntimeException("Şifre sıfırlama emaili gönderilemedi");
            }

            return new AuthResponse(
                    true,
                    "Şifre sıfırlama linki email adresinize gönderildi",
                    null,
                    user.getId()
            );

        } catch (Exception e) {
            log.error("Password reset initiation failed for email {}: {}", email, e.getMessage());
            throw new RuntimeException("Şifre sıfırlama işlemi başlatılamadı: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponse resetPassword(String token, String newPassword) {
        try {
            User user = userRepository.findByResetPasswordToken(token)
                    .orElseThrow(() -> new RuntimeException("Geçersiz şifre sıfırlama linki"));

            // Token süresini kontrol et
            if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Şifre sıfırlama linkinin süresi dolmuş");
            }

            // Yeni şifreyi doğrula
            if (newPassword == null || newPassword.trim().length() < 6) {
                throw new RuntimeException("Şifre en az 6 karakter olmalıdır");
            }

            // Şifreyi güncelle
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiry(null);
            userRepository.save(user);

            log.info("Password reset successful for user: {}", user.getEmail());

            return new AuthResponse(
                    true,
                    "Şifreniz başarıyla güncellendi",
                    user.getRole().getName().toString(),
                    user.getId()
            );

        } catch (Exception e) {
            log.error("Password reset failed for token {}: {}", token, e.getMessage());
            throw new RuntimeException("Şifre sıfırlama işlemi başarısız: " + e.getMessage());
        }
    }
}