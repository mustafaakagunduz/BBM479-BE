package com.sms.hrsam.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String text;
    private Long skillId;
    private List<OptionDTO> options;
}