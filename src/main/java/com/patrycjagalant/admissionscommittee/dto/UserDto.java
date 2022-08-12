package com.patrycjagalant.admissionscommittee.dto;

import com.patrycjagalant.admissionscommittee.annotations.PasswordMatcher;
import com.patrycjagalant.admissionscommittee.annotations.ValidEmail;
import com.patrycjagalant.admissionscommittee.entity.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@PasswordMatcher
public class UserDto {

    @EqualsAndHashCode.Exclude
    private Long id;

    @NotBlank(message = "Please provide username")
    @Size(min = 2, max = 50, message = "Username should be between 2-50 characters long")
    @Pattern(regexp = "^[\\p{L}\\d_-]{2,}$",
            message = "Username can consist only of letters (upper- and lowercase), numbers and '_' '-' characters.")
    private String username;

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