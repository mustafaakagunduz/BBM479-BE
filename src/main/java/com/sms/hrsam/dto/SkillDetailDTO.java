package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillDetailDTO {
    private Long userId;
    private String userName;  // Kullanıcı adını da gösterelim
    private Integer score;
}