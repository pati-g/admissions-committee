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
@Table(name = "applicants", indexes = {
        @Index(name = "user_ID", columnList = "user_ID", unique = true)
})
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "educational_institution", nullable = false)
    private String educationalInstitution;

    @Column(name = "certificate")
    private String certificate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_ID", nullable = false)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "applicant")
    @ToString.Exclude
    private Set<Score> scores = new LinkedHashSet<>();

    @OneToMany(mappedBy = "applicant")
    @ToString.Exclude
    private Set<Registration> registrations = new LinkedHashSet<>();
}