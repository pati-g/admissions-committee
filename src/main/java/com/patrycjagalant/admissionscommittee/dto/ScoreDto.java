package com.patrycjagalant.admissionscommittee.dto;

import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScoreDto {

    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private long id;

    @NotNull
    private Long applicantId;

    @NotBlank(message = "{validation.subject.name.not.blank}")
    @Size(min = 2, max = 150, message = "{validation.subject.name.size}")
    private String subjectName;

    @NotNull(message = "{validation.subject.result.not.null}")
    @Min(value = 0, message = "{validation.subject.result.min}")
    @Max(value = 100, message = "{validation.subject.result.max}")
    private Integer result;
}