package com.sms.hrsam.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    private UserDTO user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {
        private Long id;
        private String email;
        private String username;
        private RoleDTO role;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDTO {
        private String name;
    }

    // Constructor for backward compatibility
    public AuthResponse(boolean success, String message, String role, Long userId) {
        this.success = success;
        this.message = message;
        if (userId != null) {
            UserDTO user = new UserDTO();
            user.setId(userId);
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(role);
            user.setRole(roleDTO);
            this.user = user;
        }
    }
}