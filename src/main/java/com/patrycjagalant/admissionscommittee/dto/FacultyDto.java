package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FacultyDto {

    @EqualsAndHashCode.Exclude
    private Long id;

    @NotBlank(message = "Please enter faculty name")
    @Size(min = 2, max = 150, message = "Name should be between 2-150 characters long")
    private String name;

    @NotNull(message = "Please enter the number of budget places")
    @Min(value = 0, message = "Value can't be less than 0")
    @Max(value = 1000, message = "Value can't be more than 1 000")
    private Integer budgetPlaces;

    @NotNull(message = "Please enter the total number of places")
    @Min(value = 0, message = "Value can't be less than 0")
    @Max(value = 1000, message = "Value can't be more than 1 000")
    private Integer totalPlaces;

}