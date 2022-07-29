package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private Role role;
    private ApplicantDTO applicant;
}