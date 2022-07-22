package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "faculties")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "budget_places", nullable = false)
    private Integer budgetPlaces;

    @Column(name = "total_places", nullable = false)
    private Integer totalPlaces;

}