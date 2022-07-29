package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class ScoreDTO {

     private Long id;
    private ApplicantDTO applicant;
    private String subjectName;
    private Character gradeOrScore;
    private String result;

}