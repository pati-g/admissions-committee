package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationRequestDto {

    @EqualsAndHashCode.Exclude
    private long id;

    private ApplicantDto applicant;

    private FacultyDto faculty;

    @EqualsAndHashCode.Exclude
    private Instant registrationDate;
}