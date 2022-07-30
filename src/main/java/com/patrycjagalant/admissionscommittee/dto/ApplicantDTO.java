package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Component
public class ApplicantDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String city;
    private String region;
    private String educationalInstitution;
    private String certificate;
    private boolean isBlocked;
    private UserDTO user;
    private Set<ScoreDTO> scores = new LinkedHashSet<>();
    private Set<ApplicationRequestDTO> applicationRequests = new LinkedHashSet<>();
}