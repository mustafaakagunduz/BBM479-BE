package com.sms.hrsam.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final String frontendUrl;
    private final String fromEmail;
    private final String fromName;

    @Autowired
    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.frontend.url:https://hrsam.vercel.app}") String frontendUrl,
            @Value("${spring.mail.username}") String fromEmail,
            @Value("${app.mail.sender.name:SkillFit}") String fromName) {
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    /**
     * Send simple email with given subject and text
     * @param to recipient email address
     * @param subject email subject
     * @param text email content
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            // MimeMessage kullanarak daha fazla özelleştirme yapabiliyoruz
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            // Gönderen adını ayarlayın
            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false); // HTML içeriği değil

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Email gönderilemedi");
        }
    }

    /**
     * Send verification code email
     * @param to recipient email address
     * @param code verification code
     */
    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(to);
            helper.setSubject("Email Doğrulama Kodu");

            String content = String.format(
                    "Merhaba,\n\n" +
                            "Email adresinizi doğrulamak için aşağıdaki kodu kullanınız:\n\n" +
                            "%s\n\n" +
                            "Bu kod 15 dakika geçerlidir.\n\n" +
                            "İyi günler,\nSkillFit Ekibi",
                    code
            );

            helper.setText(content, false);

            mailSender.send(message);
            log.info("Verification code email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send verification code email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Doğrulama kodu gönderilemedi");
        }
    }

    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(to);
            helper.setSubject("Şifre Sıfırlama");

            String content = String.format(
                    "Merhaba,\n\n" +
                            "Şifrenizi sıfırlamak için aşağıdaki linke tıklayın:\n" +
                            "%s/reset-password/%s\n\n" +
                            "Bu link 1 saat geçerlidir.\n\n" +
                            "Eğer bu işlemi siz yapmadıysanız, bu emaili görmezden gelebilirsiniz.\n\n" +
                            "İyi günler,\nSkillFit Ekibi",
                    frontendUrl,
                    token
            );

            helper.setText(content, false);

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Şifre sıfırlama emaili gönderilemedi");
        }
    }

    public void sendVerificationEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(to);
            helper.setSubject("Email Doğrulama");

            String content = String.format(
                    "Merhaba,\n\n" +
                            "Email adresinizi doğrulamak için aşağıdaki linke tıklayın:\n" +
                            "%s/verify/%s\n\n" +
                            "Bu link 24 saat geçerlidir.\n\n" +
                            "İyi günler,\nSkillFit Ekibi",
                    frontendUrl,
                    token
            );

            helper.setText(content, false);

            mailSender.send(message);
            log.info("Verification email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Email gönderimi başarısız oldu");
        }
    }
}