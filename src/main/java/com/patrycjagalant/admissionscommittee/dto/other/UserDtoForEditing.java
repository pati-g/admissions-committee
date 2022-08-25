package com.patrycjagalant.admissionscommittee.dto.other;

import com.patrycjagalant.admissionscommittee.annotations.EmptyOrValid;
import com.patrycjagalant.admissionscommittee.annotations.PasswordMatcher;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@PasswordMatcher
public class UserDtoForEditing {

    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private Long id;

    private String username;

    @EmptyOrValid(message = "{validation.email.not.blank}")
    private String email;

    @EmptyOrValid(message = "{validation.password.not.blank}")
    @ToStringExclude
    private String password;

    @EmptyOrValid(message = "{validation.password.repeat}")
    @ToStringExclude
    private String matchingPassword;
}
