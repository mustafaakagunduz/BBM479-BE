package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for password reset request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    private String password;
}