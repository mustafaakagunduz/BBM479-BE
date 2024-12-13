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
@Table(name = "profession")
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;

    @ManyToMany
    @JoinTable(
            name = "profession_skill",
            joinColumns = @JoinColumn(name = "profession_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "skill_id", nullable = true)
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Skill> skills;

    @ManyToMany(mappedBy = "professions")
    private List<Survey> surveys;

    @PreRemove
    private void checkIfUsedInSurvey() {
        if (!surveys.isEmpty()) {
            throw new IllegalStateException("Bu meslek (profession) sistemde bir anket ile ilişkili olduğu için silinemez.");
        }
    }
}