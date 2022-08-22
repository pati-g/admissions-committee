package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FacultyDto {

    @EqualsAndHashCode.Exclude
    private Long id;

    @NotBlank(message = "{validation.faculty.name.not.blank}")
    @Size(min = 2, max = 150, message = "{validation.faculty.name.size}")
    private String name;

    @NotNull(message = "{validation.faculty.budget.not.blank}")
    @Min(value = 0, message = "{validation.faculty.min}")
    @Max(value = 1000, message = "{validation.faculty.max}")
    private Integer budgetPlaces;

    @NotNull(message = "{validation.faculty.total.not.blank}")
    @Min(value = 0, message = "{validation.faculty.min}")
    @Max(value = 1000, message = "{validation.faculty.max}")
    private Integer totalPlaces;

    @EqualsAndHashCode.Exclude
    private List<EnrollmentRequestDto> requests;

    @EqualsAndHashCode.Exclude
    private Set<SubjectDto> subjects;
}