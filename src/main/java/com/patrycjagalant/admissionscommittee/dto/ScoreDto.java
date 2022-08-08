package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScoreDto {

    private long id;

    @NotBlank
    private ApplicantDto applicant;

    @NotBlank
    private String subjectName;

    @NotBlank
    private Character gradeOrScore;

    @NotBlank
    private String result;

}