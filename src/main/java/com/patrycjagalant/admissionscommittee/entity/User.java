package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users", indexes = {
        @Index(name = "email", columnList = "email", unique = true),
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "E-mail is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "password")
    private String password;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Column(name = "is_blocked")
    private boolean isBlocked;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    private Applicant applicant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}