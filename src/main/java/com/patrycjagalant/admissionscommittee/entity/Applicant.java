package com.patrycjagalant.admissionscommittee.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
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

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "City is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "city")
    private String city;

    @NotBlank(message = "Region is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "region")
    private String region;

    @NotBlank(message = "Educational institution is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "educational_institution")
    private String educationalInstitution;

    @Size(min = 2, max = 255)
    @Column(name = "certificate")
    private String certificate;

    @NotNull
    @Column(name = "is_blocked")
    private boolean isBlocked;

    @NotBlank
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Applicant applicant = (Applicant) o;
        return id != null && Objects.equals(id, applicant.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}