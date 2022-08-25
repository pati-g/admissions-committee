package com.patrycjagalant.admissionscommittee.dto;

import com.patrycjagalant.admissionscommittee.entity.Status;
import lombok.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EnrollmentRequestDto {

    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private long id;

    @NotBlank
    private ApplicantDto applicant;

    @NotBlank
    private FacultyDto faculty;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private String registrationDate;

    @NotNull(message = "{validation.points.not.null}")
    @Min(value = 0, message = "{validation.points.min}")
    @Max(value = 100, message = "{validation.points.max}")
    private Integer points = -1;

    @NotBlank
    private Status status = Status.PENDING;

    private Status tempStatus = Status.PENDING;
}