package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplicantDto {

    @EqualsAndHashCode.Exclude
    private long id;

    private String fullName;

    @NotBlank(message = "{validation.firstname.not.blank}")
    @Size(min = 2, max = 255, message = "{validation.fname.size}")
    private String firstName;

    @NotBlank(message = "{validation.lastname.not.blank}")
    @Size(min = 2, max = 255, message = "{validation.lname.size}")
    private String lastName;

    @NotBlank(message = "{validation.city.not.blank}")
    @Size(min = 2, max = 255, message = "{validation.city.size}")
    private String city;

    @NotBlank(message = "{validation.region.not.blank}")
    @Size(min = 2, max = 255, message = "{validation.region.size}")
    private String region;

    @NotBlank(message = "{validation.edu.not.blank}")
    @Size(min = 2, max = 255, message = "{validation.edu.size}")
    private String educationalInstitution;

    private UserDto userDetails;

    private List<ScoreDto> scores;

    private List<EnrollmentRequestDto> requests;

    private String certificateUrl;

    public ApplicantDto(UserDto userDetails, List<ScoreDto> scores, List<EnrollmentRequestDto> requests) {
        this.userDetails = userDetails;
        this.scores = scores;
        this.requests = requests;
    }
}