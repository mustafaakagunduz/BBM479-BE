package com.sms.hrsam.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "survey_result",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "survey_id", "attempt_number"},
                        name = "uk_survey_result_user_survey_attempt"
                )
        })
public class SurveyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "surveyResult",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ProfessionMatch> professionMatches = new ArrayList<>();

    private LocalDateTime createdAt;

    private Integer attemptNumber;

    // Helper methods
    public void addProfessionMatch(ProfessionMatch match) {
        professionMatches.add(match);
        match.setSurveyResult(this);
    }

    @OneToMany(mappedBy = "surveyResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionResult> questionResults = new ArrayList<>();

    // Yeni eklenen preRemove metodu
    @PreRemove
    public void preRemove() {
        for (ProfessionMatch match : professionMatches) {
            match.setSurveyResult(null);
        }
    }
}