package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ApplicantDTO {

    @NotBlank
    private long id;
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
}