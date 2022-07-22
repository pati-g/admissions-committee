package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "statements", indexes = {
        @Index(name = "registration_ID", columnList = "registration_ID")
})
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_ID", nullable = false)
    @ToString.Exclude
    private Registration registration;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "enrollment", nullable = false)
    private Character enrollment;
}