package com.sms.hrsam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;

    @OneToMany(mappedBy = "skill")
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private List<Question> questions;

    @PreRemove
    private void checkIfUsedInSurvey() {
        if (questions.stream().anyMatch(question -> question.getSurvey() != null)) {
            throw new IllegalStateException("Bu yetenek (skill) bir ankette kullanılıyor ve silinemez.");
        }
    }
}
