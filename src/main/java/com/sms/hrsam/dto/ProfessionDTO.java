// IndustryDTO.java
package com.sms.hrsam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionDTO {
    private Long id;
    private String name;
    private Long industryId;
    private String industryName;
    private List<RequiredLevelDTO> requiredSkills;
}