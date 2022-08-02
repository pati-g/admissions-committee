package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String city;
    @NotBlank
    private String region;
    @NotBlank
    private String educationalInstitution;
    private String certificate;
    private boolean isBlocked;
    @NotBlank
    private UserDTO user;
    private Set<ScoreDTO> scores = new LinkedHashSet<>();
    private Set<ApplicationRequestDTO> applicationRequests = new LinkedHashSet<>();
}