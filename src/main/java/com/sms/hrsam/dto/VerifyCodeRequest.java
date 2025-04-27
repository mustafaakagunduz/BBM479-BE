package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for email verification code validation request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeRequest {
    private String email;
    private String code;
}