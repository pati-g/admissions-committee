package com.patrycjagalant.admissionscommittee.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Faculty name is mandatory")
    @Size(min = 2, max = 255)
    @Column(name = "name")
    private String name;

    @NotNull(message = "Budget places number is mandatory")
    @Min(1)
    @Column(name = "budget_places")
    private Integer budgetPlaces;

    @NotNull(message = "Total places number is mandatory")
    @Min(1)
    @Column(name = "total_places")
    private Integer totalPlaces;

}