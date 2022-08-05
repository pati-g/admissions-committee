package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FacultyDTO {

    @NotNull
    private long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer budgetPlaces;
    @NotNull
    private Integer totalPlaces;

}