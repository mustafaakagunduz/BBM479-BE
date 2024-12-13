package com.sms.hrsam.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey")
@Data
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "industry_id", nullable = true)
    private Industry industry;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "survey_professions",
            joinColumns = @JoinColumn(name = "survey_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "profession_id", nullable = true)
    )
    private List<Profession> professions = new ArrayList<>();

    // Survey silindiğinde sonuçların da silinmesi için cascade ayarı
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyResult> surveyResults = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        // ManyToMany ilişkisini temizle
        professions.clear();
        // Industry ilişkisini null yap
        industry = null;
    }
}