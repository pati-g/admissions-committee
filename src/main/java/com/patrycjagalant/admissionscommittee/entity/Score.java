package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
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

    @NotBlank(message = "Please provide a valid result")
    @Column(name = "result", nullable = false)
    private Integer result;

}