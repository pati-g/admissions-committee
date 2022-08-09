package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatementDto {

    @EqualsAndHashCode.Exclude
    private long id;

    private ApplicationRequestDto applicationRequest;

    @NotNull(message = "Please enter the number of points")
    @Min(value = 0, message = "Minimum points value is 0")
    @Max(value = 100, message = "Maximum points value is 100")
    private Integer points;

    @NotBlank
    private Character enrollment;
}