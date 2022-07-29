package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class ApplicationRequestDTO {

    private Long id;
    private ApplicantDTO applicant;
    private FacultyDTO faculty;
    private Instant registrationDate;
}