package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;
import org.springframework.stereotype.Component;

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
    private Integer points;
    private Character enrollment;
}