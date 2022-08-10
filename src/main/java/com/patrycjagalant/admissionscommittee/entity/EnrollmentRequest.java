package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "Enrollment_Requests", indexes = {
        @Index(name = "faculty_ID", columnList = "faculty_ID"),
        @Index(name = "applicant_ID", columnList = "applicant_ID"),
})
public class EnrollmentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicant_ID")
    @ToString.Exclude
    private Applicant applicant;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_ID")
    @ToString.Exclude
    private Faculty faculty;

    @NotBlank
    @Column(name = "requested_on")
    private Instant registrationDate;

    @NotNull(message = "Please provide a valid number of points (only integers are allowed)")
    @Min(0)
    @Column(name = "points")
    private Integer points = -1;

    @NotBlank
    @Column(name = "status")
    private Character status = 'P';
}