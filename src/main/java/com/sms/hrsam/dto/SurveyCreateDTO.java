package com.sms.hrsam.dto;

import lombok.Data;

import java.util.List;

// DTO classes for request/response
@Data
public class SurveyCreateDTO {
    private String title;
    private List<QuestionCreateDTO> questions;
}