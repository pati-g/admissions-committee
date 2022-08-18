package com.patrycjagalant.admissionscommittee.dto.other;

import com.patrycjagalant.admissionscommittee.entity.Status;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestWithNamesDto {

    @EqualsAndHashCode.Exclude
    private long id;

    private String applicant;

    private String faculty;

    @EqualsAndHashCode.Exclude
    private LocalDateTime date;

    private Integer points;

    private Status status;
}
