package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.*;
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
    @Min(value = 0, message = "Minimum points value is 0")
    @Max(value = 100, message = "Maximum points value is 100")
    @Column(name = "points")
    private Integer points = -1;

    @NotBlank
    private Character status = 'P';
}