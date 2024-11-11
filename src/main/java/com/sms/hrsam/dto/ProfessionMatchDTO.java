package com.sms.hrsam.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfessionMatchDTO {
    private Long professionId;
    private String professionName;
    private Double matchPercentage;
}