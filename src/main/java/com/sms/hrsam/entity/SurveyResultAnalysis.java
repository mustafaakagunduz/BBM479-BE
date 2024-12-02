// Company.java
package com.sms.hrsam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "survey_result_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResultAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "survey_result_id")
    private Long surveyResultId;

    @Column(columnDefinition = "TEXT")
    private String analysisText;

    @ElementCollection
    @CollectionTable(
            name = "survey_result_analysis_recommendations",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "recommendation")
    private List<String> recommendations;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "survey_result_id", insertable = false, updatable = false)
    private SurveyResult surveyResult;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}