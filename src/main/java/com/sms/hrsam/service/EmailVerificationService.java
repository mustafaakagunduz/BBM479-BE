package com.sms.hrsam.service;

import com.sms.hrsam.entity.User;
import com.sms.hrsam.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for email verification with 6-digit code
 */
@Service
@Slf4j
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    // Store verification codes and expiry times in memory
    // In a production environment, this should be stored in a persistent storage
    private final Map<String, VerificationData> verificationCodes = new HashMap<>();

    @Autowired
    public EmailVerificationService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    /**
     * Generate a random 6-digit verification code
     * @return 6-digit verification code
     */
    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
        return String.valueOf(code);
    }

    /**
     * Send verification code to the user's email
     * @param user the user to send verification code to
     * @return the generated verification code
     */
    public String sendVerificationCode(User user) {
        String code = generateVerificationCode();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15); // Code expires in 15 minutes

        // Store verification data
        verificationCodes.put(user.getEmail(), new VerificationData(code, expiryTime));

        try {
            // Send the verification code via email
            sendVerificationEmail(user.getEmail(), code);
            log.info("Verification code sent to: {}", user.getEmail());
            return code;
        } catch (Exception e) {
            log.error("Failed to send verification code to {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send verification code");
        }
    }

    /**
     * Send verification code email
     * @param email recipient email
     * @param code verification code
     */
    private void sendVerificationEmail(String email, String code) {
        try {
            String subject = "Email Doğrulama Kodu";
            String content = String.format(
                    "Merhaba,\n\n" +
                            "Email adresinizi doğrulamak için aşağıdaki kodu kullanınız:\n\n" +
                            "%s\n\n" +
                            "Bu kod 15 dakika geçerlidir.\n\n" +
                            "İyi günler,\nHR-SAM Ekibi",
                    code
            );

            // EmailService üzerinden e-posta gönder
            emailService.sendSimpleEmail(email, subject, content);
            log.info("Verification email sent to {} with code {}", email, code);
        } catch (Exception e) {
            log.error("Failed to send verification email: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Verify the code submitted by the user
     * @param email the user's email
     * @param code the verification code submitted by the user
     * @return true if the code is valid, false otherwise
     */
    public boolean verifyCode(String email, String code) {
        VerificationData data = verificationCodes.get(email);

        if (data == null) {
            log.warn("No verification code found for email: {}", email);
            return false;
        }

        if (LocalDateTime.now().isAfter(data.expiryTime)) {
            log.warn("Verification code has expired for email: {}", email);
            verificationCodes.remove(email);
            return false;
        }

        boolean isValid = data.code.equals(code);

        if (isValid) {
            // Remove the code after successful verification
            verificationCodes.remove(email);
            log.info("Verification code successfully validated for: {}", email);
        } else {
            log.warn("Invalid verification code submitted for: {}", email);
        }

        return isValid;
    }

    /**
     * Verify code and return user credentials for auto-login
     * @param email User email
     * @param code Verification code
     * @return User credentials for auto-login or null if verification fails
     */
    public User verifyCodeAndGetUserForLogin(String email, String code) {
        boolean isValid = verifyCode(email, code);

        if (isValid) {
            // Mark email as verified
            boolean updated = markEmailAsVerified(email);

            if (updated) {
                // Return user for auto-login
                return userRepository.findByEmail(email).orElse(null);
            }
        }

        return null;
    }

    /**
     * Mark user's email as verified in the database
     * @param email the email to mark as verified
     * @return true if the user was found and updated, false otherwise
     */
    @Transactional
    public boolean markEmailAsVerified(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            log.warn("User not found with email: {}", email);
            return false;
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);

        log.info("Email verified for user: {}", email);
        return true;
    }

    /**
     * Inner class to store verification code and its expiry time
     */
    private static class VerificationData {
        private final String code;
        private final LocalDateTime expiryTime;

        public VerificationData(String code, LocalDateTime expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }
    }
}