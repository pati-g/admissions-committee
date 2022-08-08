package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationRequestDto {

    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank
    private ApplicantDto applicant;

    @NotBlank
    private FacultyDto faculty;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private Instant registrationDate;
}