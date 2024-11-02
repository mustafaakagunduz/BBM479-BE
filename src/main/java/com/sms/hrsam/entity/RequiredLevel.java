package com.sms.hrsam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;  // No-arg constructor için ekledik
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "required_level")
@Data
@AllArgsConstructor

@NoArgsConstructor  // Parametresiz constructor ekledik

@Builder
public class RequiredLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Min(1)
    @Max(5)
    private Integer requiredLevel;
}
