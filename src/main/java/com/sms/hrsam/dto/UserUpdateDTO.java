package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
        private String name;
        private String email;
        private String username;
        private Long companyId;
}
