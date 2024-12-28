// Company.java
package com.sms.hrsam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "survey_result_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // Bu satırı ekleyin
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
    private List<String> recommendations = new ArrayList<>();  // Boş liste ile başlat

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_result_id", insertable = false, updatable = false)
    @JsonIgnore  // Bu annotation'ı ekleyin
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