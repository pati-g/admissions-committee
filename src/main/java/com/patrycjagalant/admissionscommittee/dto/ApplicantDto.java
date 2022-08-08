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

    @NotBlank(message = "Please enter first name")
    @Size(min = 2, max = 255, message = "Name should be between 2-255 characters long")
    private String firstName;

    @NotBlank(message = "Please enter last name")
    @Size(min = 2, max = 255, message = "Name should be between 2-255 characters long")
    private String lastName;

    @NotBlank(message = "Please enter city name")
    @Size(min = 2, max = 255, message = "Name should be between 2-255 characters long")
    private String city;

    @NotBlank(message = "Please enter region name")
    @Size(min = 2, max = 255, message = "Name should be between 2-255 characters long")
    private String region;

    @NotBlank(message = "Please enter the name of educational institution")
    @Size(min = 2, max = 255, message = "Name should be between 2-255 characters long")
    private String educationalInstitution;

    private String email;

    private String certificate;
}