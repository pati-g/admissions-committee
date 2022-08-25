package com.patrycjagalant.admissionscommittee.dto.other;

import com.patrycjagalant.admissionscommittee.entity.Status;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestWithNamesDto {

    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private long id;

    private String applicant;

    private String faculty;

    @EqualsAndHashCode.Exclude
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    private Integer points;

    private Status status;
}
