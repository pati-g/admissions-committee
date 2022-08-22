package com.patrycjagalant.admissionscommittee.dto.other;

import com.patrycjagalant.admissionscommittee.annotations.EmptyOrValid;
import com.patrycjagalant.admissionscommittee.annotations.PasswordMatcher;
import com.patrycjagalant.admissionscommittee.annotations.ValidEmail;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@PasswordMatcher
public class UserDtoForEditing {
    @EqualsAndHashCode.Exclude
    private Long id;

    private String username;

    @EmptyOrValid(message = "{validation.email.not.blank}")
    @ValidEmail(message = "{validation.email}")
    private String email;

    @EmptyOrValid(message = "{validation.password.not.blank}")
    @ToStringExclude
    private String password;

    @EmptyOrValid(message = "{validation.password.repeat}")
    @ToStringExclude
    private String matchingPassword;
}
