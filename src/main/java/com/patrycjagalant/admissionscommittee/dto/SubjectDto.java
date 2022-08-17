package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubjectDto {

    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank(message = "Please enter subject name")
    @Size(min = 2, max = 150, message = "Name should be between 2-150 characters long")
    private String name;

    @NotBlank
    private List<FacultyDto> faculties;

}
