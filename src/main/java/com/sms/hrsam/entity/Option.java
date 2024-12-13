package com.sms.hrsam.entity;

import lombok.Data;
import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
@Entity
@Table(name = "option")
@Data
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    private Integer level;

    private String description;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
