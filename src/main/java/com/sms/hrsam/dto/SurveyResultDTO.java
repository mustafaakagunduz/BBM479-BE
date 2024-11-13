package com.sms.hrsam.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SurveyResultDTO {
    private Long id;
    private Long userId;
    private Long surveyId;
    private Integer attemptNumber;
    private List<ProfessionMatchDTO> professionMatches;
    private LocalDateTime createdAt;
}