package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

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
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicant_ID", nullable = false)
    @ToString.Exclude
    private Applicant applicant;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "grade_or_score", nullable = false)
    private Character gradeOrScore;

    @Column(name = "result", nullable = false, length = 4)
    private String result;

    @OneToMany(mappedBy = "scores")
    @ToString.Exclude
    private Set<Registration> registrations = new LinkedHashSet<>();
}