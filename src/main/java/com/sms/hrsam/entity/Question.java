package com.sms.hrsam.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill; // Skill sınıfının import edilmesi gerekiyor

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey; // Survey sınıfının import edilmesi gerekiyor

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options = new ArrayList<>(); // Option sınıfının import edilmesi gerekiyor
}
