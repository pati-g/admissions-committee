package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StatementDTO {

    @NotBlank
    private long id;
    private ApplicationRequestDTO applicationRequest;
    @NotBlank
    private Integer points;
    @NotBlank
    private Character enrollment;
}