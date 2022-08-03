package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Application_Requests", indexes = {
        @Index(name = "faculty_ID", columnList = "faculty_ID"),
        @Index(name = "applicant_ID", columnList = "applicant_ID"),
})
public class ApplicationRequest {
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
    @Column(name = "registration_date")
    private Instant registrationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationRequest that = (ApplicationRequest) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}