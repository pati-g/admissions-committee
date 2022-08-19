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
    private Long id;

    private String username;

    @EmptyOrValid
    private String email;

    @EmptyOrValid
    @ToStringExclude
    private String password;

    @EmptyOrValid
    @ToStringExclude
    private String matchingPassword;
}
