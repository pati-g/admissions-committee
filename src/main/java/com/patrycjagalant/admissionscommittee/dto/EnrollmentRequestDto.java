package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EnrollmentRequestDto {

    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank
    private ApplicantDto applicant;

    @NotBlank
    private FacultyDto faculty;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private LocalDateTime registrationDate;

    @NotNull(message = "Please provide a valid number of points (only integers are allowed)")
    @Size(min = 1)
    @Column(name = "points")
    private Integer points = -1;

    @NotBlank
    private Character status = 'P';
}