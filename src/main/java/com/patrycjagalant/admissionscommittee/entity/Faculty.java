package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "name")
    private String name;

    @NotNull
    @Min(1)
    @Column(name = "budget_places")
    private Integer budgetPlaces;

    @NotNull
    @Min(1)
    @Column(name = "total_places")
    private Integer totalPlaces;

}