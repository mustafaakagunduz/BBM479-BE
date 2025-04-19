package com.sms.hrsam.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final String frontendUrl;

    @Autowired
    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.frontend.url:https://hrsam.vercel.app}") String frontendUrl) {
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
    }
    public void sendPasswordResetEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Şifre Sıfırlama");
            message.setText(
                    String.format(
                            "Merhaba,\n\n" +
                                    "Şifrenizi sıfırlamak için aşağıdaki linke tıklayın:\n" +
                                    "%s/reset-password/%s\n\n" +
                                    "Bu link 1 saat geçerlidir.\n\n" +
                                    "Eğer bu işlemi siz yapmadıysanız, bu emaili görmezden gelebilirsiniz.\n\n" +
                                    "İyi günler,\nHR-SAM Ekibi",
                            frontendUrl,
                            token
                    )
            );

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Şifre sıfırlama emaili gönderilemedi");
        }
    }
    public void sendVerificationEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Email Doğrulama");
            message.setText(
                    String.format(
                            "Merhaba,\n\n" +
                                    "Email adresinizi doğrulamak için aşağıdaki linke tıklayın:\n" +
                                    "%s/verify/%s\n\n" +
                                    "Bu link 24 saat geçerlidir.\n\n" +
                                    "İyi günler,\nHR-SAM Ekibi",
                            frontendUrl,
                            token
                    )
            );

            mailSender.send(message);
            log.info("Verification email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Email gönderimi başarısız oldu");
        }
    }
}