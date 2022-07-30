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
public class FacultyDTO {

    private Long id;
    private String name;
    private Integer budgetPlaces;
    private Integer totalPlaces;

}