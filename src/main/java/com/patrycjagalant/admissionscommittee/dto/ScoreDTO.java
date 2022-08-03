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
public class ScoreDTO {

    private ApplicantDTO applicant;
    @NotBlank
    private String subjectName;
    @NotBlank
    private Character gradeOrScore;
    @NotBlank
    private String result;

}