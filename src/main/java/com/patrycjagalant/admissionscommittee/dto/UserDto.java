package com.patrycjagalant.admissionscommittee.dto;

import com.patrycjagalant.admissionscommittee.annotations.PasswordMatcher;
import com.patrycjagalant.admissionscommittee.annotations.ValidEmail;
import com.patrycjagalant.admissionscommittee.entity.Role;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@PasswordMatcher(message = "{validation.password.match}")
public class UserDto {

    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "{validation.username.not.blank}")
    @Size(min = 2, max = 50, message = "{validation.username.size}")
    @Pattern(regexp = "^[\\p{L}\\d_.-]{2,}$",
            message = "{validation.username.pattern}")
    private String username;

    @NotBlank(message = "{validation.email}")
    @ValidEmail
    private String email;

    @NotBlank(message = "{validation.password.not.blank}")
    @ToStringExclude
    @Size(min = 8, max = 127, message = "{validation.password.size}")
    private String password;

    @NotBlank(message = "{validation.password.repeat}")
    @ToStringExclude
    @Size(min = 8, max = 127)
    private String matchingPassword;

    private boolean isBlocked = false;

    private boolean isEnabled = true;

    private Role role = Role.USER;

    private ApplicantDto applicant;

}