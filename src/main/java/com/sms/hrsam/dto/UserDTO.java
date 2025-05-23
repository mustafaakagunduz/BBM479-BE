package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String role;
    private String profileImage;
    private CompanyDTO company;
    private boolean emailVerified;
}