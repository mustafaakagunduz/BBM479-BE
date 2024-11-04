package com.sms.hrsam.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequiredLevelDTO {
    private Long id;
    private Long skillId;
    private String skillName;
    private Integer requiredLevel;
}