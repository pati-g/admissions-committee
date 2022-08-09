package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScoreDto {

    private long id;

    @NotBlank
    private ApplicantDto applicant;

    @NotBlank(message = "Please enter subject name")
    @Size(min = 2, max = 150, message = "Name should be between 2-150 characters long")
    private String subjectName;

    @NotBlank(message = "Please enter the result")
    @Min(value = 0, message = "Minimum value of the result should be 0")
    @Max(value = 100, message = "Maximum value of the result should be 100")
    private Integer result;

}