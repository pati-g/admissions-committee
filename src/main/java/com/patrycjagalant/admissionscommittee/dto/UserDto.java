package com.patrycjagalant.admissionscommittee.dto;

import com.patrycjagalant.admissionscommittee.annotations.PasswordMatcher;
import com.patrycjagalant.admissionscommittee.annotations.ValidEmail;
import com.patrycjagalant.admissionscommittee.entity.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@PasswordMatcher
public class UserDto {

    @EqualsAndHashCode.Exclude
    private Long id;

    @ValidEmail(message = "Please provide a valid e-mail address")
    private String email;

    @NotBlank(message = "Please enter password")
    @Size(min = 8, max = 127, message = "Password should be between 8-127 characters long")
    private String password;

    @NotBlank(message = "Please re-enter password")
    @Size(min = 8, max = 127)
    private String matchingPassword;

    private boolean isBlocked = false;

    private boolean isEnabled = true;

    private Role role = Role.USER;

    private ApplicantDto applicant;

}