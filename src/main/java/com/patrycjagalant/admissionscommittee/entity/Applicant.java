package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "applicants", indexes = {
        @Index(name = "user_ID", columnList = "user_ID", unique = true)
})
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Size(min = 2, max = 255)
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 2, max = 255)
    @Column(name = "last_name")
    private String lastName;

    @Size(min = 2, max = 255)
    @Column(name = "city")
    private String city;

    @Size(min = 2, max = 255)
    @Column(name = "region")
    private String region;

    @Size(min = 2, max = 255)
    @Column(name = "educational_institution")
    private String educationalInstitution;

    @Size(min = 2, max = 255)
    @Column(name = "certificate")
    private String certificateUrl;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_ID")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "applicant")
    @ToString.Exclude
    private List<Score> scores;

    @OneToMany(mappedBy = "applicant")
    @ToString.Exclude
    private List<EnrollmentRequest> enrollmentRequests;

}