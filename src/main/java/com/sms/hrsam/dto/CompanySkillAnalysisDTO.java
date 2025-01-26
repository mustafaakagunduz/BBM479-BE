package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySkillAnalysisDTO {
    private Long companyId;
    private String companyName;
    private List<SkillScore> skillScores;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillScore {
        private String skillName;
        private Double averageScore;
    }
}