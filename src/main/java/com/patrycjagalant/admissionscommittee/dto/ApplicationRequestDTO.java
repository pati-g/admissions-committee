package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Component
public class ApplicationRequestDTO {

    private ApplicantDTO applicant;
    private FacultyDTO faculty;
    private Instant registrationDate;
}