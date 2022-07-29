package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_ID")
    @ToString.Exclude
    private Applicant applicant;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "grade_or_score")
    private Character gradeOrScore;

    @Column(name = "result", nullable = false, length = 4)
    private String result;

}