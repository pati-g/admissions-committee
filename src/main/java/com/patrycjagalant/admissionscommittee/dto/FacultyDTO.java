package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Component
public class FacultyDTO {

    @NotBlank
    private String name;
    @NotNull
    private Integer budgetPlaces;
    @NotNull
    private Integer totalPlaces;

}