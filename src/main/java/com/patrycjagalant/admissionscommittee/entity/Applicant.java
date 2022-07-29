package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;
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
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "city")
    private String city;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "region")
    private String region;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "educational_institution")
    private String educationalInstitution;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "certificate")
    private String certificate;

    @NotNull
    @Column(name = "is_blocked")
    private boolean isBlocked;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_ID")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "applicant")
    @ToString.Exclude
    private Set<Score> scores = new LinkedHashSet<>();

    @OneToMany(mappedBy = "applicant")
    @ToString.Exclude
    private Set<ApplicationRequest> applicationRequests = new LinkedHashSet<>();
}