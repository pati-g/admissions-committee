package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "registrations", indexes = {
        @Index(name = "faculty_ID", columnList = "faculty_ID"),
        @Index(name = "applicant_ID", columnList = "applicant_ID"),
        @Index(name = "scores_ID", columnList = "scores_ID")
})
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicant_ID", nullable = false)
    @ToString.Exclude
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_ID", nullable = false)
    @ToString.Exclude
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scores_ID", nullable = false)
    @ToString.Exclude
    private Score scores;

    @Column(name = "registration_date", nullable = false)
    private Instant registrationDate;

    @OneToMany(mappedBy = "registration")
    @ToString.Exclude
    private Set<Statement> statements = new LinkedHashSet<>();
}