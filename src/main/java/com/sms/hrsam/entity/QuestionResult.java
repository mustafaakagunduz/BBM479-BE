package com.sms.hrsam.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "question_result")
@Data
public class QuestionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_result_id")
    private SurveyResult surveyResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private Integer score;
}