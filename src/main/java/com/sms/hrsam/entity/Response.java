package com.sms.hrsam.entity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
@Entity
@Table(name = "response", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"user_id", "survey_id", "question_id", "attempt_number"},
                name = "uk_user_survey_question_attempt"
        )
})
@Data
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @Min(1)
    @Max(5)
    private Integer enteredLevel;

    @ManyToOne
    @JoinColumn(name = "survey_result_id")
    private SurveyResult surveyResult;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // Default value

    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void setSurveyResult(SurveyResult surveyResult) {
        this.surveyResult = surveyResult;
        if (surveyResult != null && !surveyResult.getResponses().contains(this)) {
            surveyResult.getResponses().add(this);
        }
    }
}