package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users", indexes = {
        @Index(name = "email", columnList = "email", unique = true),
        @Index(name = "username", columnList = "username", unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "email")
    private String email;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "password")
    private String password;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    private Applicant applicant;
}