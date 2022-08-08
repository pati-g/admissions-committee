package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplicantDto {

    @EqualsAndHashCode.Exclude
    private long id;

    private String fullName;

    @NotBlank
    @Size(min = 2, max = 127)
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String city;

    @NotBlank
    private String region;

    @NotBlank
    private String educationalInstitution;

    private String email;

    private String certificate;
}