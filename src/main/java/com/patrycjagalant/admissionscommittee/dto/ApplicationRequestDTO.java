package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ApplicationRequestDTO {

    private long id;
    private ApplicantDTO applicant;
    private FacultyDTO faculty;
    private Instant registrationDate;
}