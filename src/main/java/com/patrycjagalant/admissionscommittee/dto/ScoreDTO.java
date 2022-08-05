package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ScoreDTO {

    private long id;
    private ApplicantDTO applicant;
    @NotBlank
    private String subjectName;
    @NotBlank
    private Character gradeOrScore;
    @NotBlank
    private String result;

}