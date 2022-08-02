package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Component
public class StatementDTO {

    private Long id;
    private ApplicationRequestDTO applicationRequest;
    @NotBlank
    private Integer points;
    @NotBlank
    private Character enrollment;
}