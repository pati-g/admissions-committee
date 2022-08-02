package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "scores", indexes = {
        @Index(name = "applicant_ID", columnList = "applicant_ID")
})
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_ID")
    @ToString.Exclude
    private Applicant applicant;

    @NotBlank(message = "Please provide the name of the subject")
    @Column(name = "subject_name")
    private String subjectName;

    @NotBlank
    @Column(name = "grade_or_score")
    private Character gradeOrScore;

    @NotBlank(message = "Please provide a valid result")
    @Column(name = "result", nullable = false, length = 4)
    private String result;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Score score = (Score) o;
        return id != null && Objects.equals(id, score.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}