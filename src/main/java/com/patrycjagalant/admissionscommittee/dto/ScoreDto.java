package com.patrycjagalant.admissionscommittee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScoreDto {


    private long id;

    @NotNull
    private Long applicantId;

    @NotBlank(message = "Please enter subject name")
    @Size(min = 2, max = 150, message = "Name should be between 2-150 characters long")
    private String subjectName;

    @NotNull(message = "Please enter the result")
    @Min(value = 0, message = "Minimum value of the result should be 0")
    @Max(value = 100, message = "Maximum value of the result should be 100")
    private Integer result;

    public ScoreDto(Long applicantId) {
        this.applicantId = applicantId;
    }

}