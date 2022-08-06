package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDTO {

    private long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private Role role;
    private ApplicantDTO applicant;
}