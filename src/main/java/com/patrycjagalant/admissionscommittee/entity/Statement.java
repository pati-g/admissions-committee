package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "statements", indexes = {@Index(name = "request_ID", columnList = "request_ID")})
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "request_ID")
    @ToString.Exclude
    private EnrollmentRequest enrollmentRequest;


    @Column(name = "points")
    private Integer points;

    @Column(name = "enrollment")
    private Character enrollment;

}