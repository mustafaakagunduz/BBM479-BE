package com.sms.hrsam.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanySkillStatsDTO {
    private Long skillId;
    private String skillName;
    private Double companyAverage;
    private Double aboveAverageScore;
    private Double belowAverageScore;
    private List<UserSkillDTO> aboveAverageUsers;
}