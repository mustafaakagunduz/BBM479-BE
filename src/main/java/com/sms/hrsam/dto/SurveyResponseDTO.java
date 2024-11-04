package com.sms.hrsam.dto;

import lombok.Data;

import java.util.List;

@Data
public class SurveyResponseDTO {
    private Long userId;
    private Long surveyId;
    private List<QuestionResponseDTO> responses;
}