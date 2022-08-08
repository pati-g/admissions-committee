package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScoreDto {

    private long id;

    @NotBlank
    private ApplicantDto applicant;

    @NotBlank(message = "Please enter subject name")
    @Size(min = 2, max = 150, message = "Name should be between 2-150 characters long")
    private String subjectName;

    @NotBlank
    private Character gradeOrScore;

    @NotBlank
    private String result;

}