package com.sms.hrsam.dto;

import lombok.Data;


@Data
public class QuestionResponseDTO {
    private Long questionId;
    private Long optionId;
    private Integer selectedLevel;
}