package com.sms.hrsam.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms.hrsam.validator.ValidPassword;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String username;
    @ValidPassword
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profileImage;  // Changed from byte[] to String

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean emailVerified = true;

    private String verificationToken;
    private LocalDateTime verificationTokenExpiry;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;
    @JsonIgnoreProperties("users") // Bu satırı ekleyin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // getter ve setter
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}