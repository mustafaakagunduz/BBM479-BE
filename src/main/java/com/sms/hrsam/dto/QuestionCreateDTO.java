package com.sms.hrsam.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionCreateDTO {
    private String text;
    private Long skillId;
    private List<OptionCreateDTO> options; // OptionCreateDTO'nun import edildiğinden emin olun
}
