package com.sms.hrsam.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "profession_match")
@Data
public class ProfessionMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_result_id", nullable = false)
    private SurveyResult surveyResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profession_id")
    private Profession profession;

    @Column(nullable = false)
    private Double matchPercentage;
}