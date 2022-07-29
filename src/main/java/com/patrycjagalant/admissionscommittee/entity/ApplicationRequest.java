package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "registrations", indexes = {
        @Index(name = "faculty_ID", columnList = "faculty_ID"),
        @Index(name = "applicant_ID", columnList = "applicant_ID"),
})
public class ApplicationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicant_ID")
    @ToString.Exclude
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_ID")
    @ToString.Exclude
    private Faculty faculty;

    @NotNull
    @Column(name = "registration_date")
    private Instant registrationDate;
}