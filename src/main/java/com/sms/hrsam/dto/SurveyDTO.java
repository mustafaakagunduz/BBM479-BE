package com.sms.hrsam.dto;

import lombok.Data;
import java.util.List;

@Data
public class SurveyDTO {
    private Long id;
    private Long userId;
    private String title;
    private Long industryId;
    private List<Long> selectedProfessions;
    private List<QuestionDTO> questions;
}