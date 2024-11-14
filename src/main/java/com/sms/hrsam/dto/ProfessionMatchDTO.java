package com.sms.hrsam.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfessionMatchDTO {
    private Long id;   // Yeni eklenen
    private Long professionId;
    private String professionName;
    private double matchPercentage;
}