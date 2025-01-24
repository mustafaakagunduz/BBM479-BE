package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillDTO {
    private Long userId;
    private String userName;
    private Double skillScore;
}