package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FacultyDto {

    @EqualsAndHashCode.Exclude
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Integer budgetPlaces;

    @NotNull
    private Integer totalPlaces;

}