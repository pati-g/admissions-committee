package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatementDto {

    @EqualsAndHashCode.Exclude
    private long id;

    private ApplicationRequestDto applicationRequest;

    @NotBlank
    private Integer points;
    @NotBlank
    private Character enrollment;
}