package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicant_ID")
    @ToString.Exclude
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_ID")
    @ToString.Exclude
    private Faculty faculty;

    @CreationTimestamp
    @Column(name = "requested_on")
    private LocalDateTime registrationDate;

    @Column(name = "points", columnDefinition = "integer default -1")
    private Integer points = -1;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDING;
}