package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private Integer budgetPlaces;
    @NotBlank
    private Integer totalPlaces;

}