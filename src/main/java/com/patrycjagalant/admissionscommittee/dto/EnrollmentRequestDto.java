package com.patrycjagalant.admissionscommittee.dto;

import com.patrycjagalant.admissionscommittee.entity.Status;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private Integer points = -1;

    @NotBlank
    private Status status = Status.PENDING;

    private Status tempStatus = Status.PENDING;
}