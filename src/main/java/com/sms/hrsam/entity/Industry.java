package com.sms.hrsam.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "industry")
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;

    @OneToMany(mappedBy = "industry")
    private List<Profession> professions;

    @PreRemove
    private void checkIfUsedInProfession() {
        if (!professions.isEmpty()) {
            throw new IllegalStateException("Bu sektör (industry) bir meslek ile ilişkili olduğu için silinemez.");
        }
    }
}