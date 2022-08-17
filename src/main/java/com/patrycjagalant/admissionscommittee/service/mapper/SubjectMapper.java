package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.SubjectDto;
import com.patrycjagalant.admissionscommittee.entity.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SubjectMapper {

    public SubjectDto mapToDto (Subject subject) {
        FacultyMapper facultyMapper = new FacultyMapper();
        log.debug("Subject entity before mapping: {}", subject);
         return SubjectDto.builder()
                 .id(subject.getId())
                 .name(subject.getName())
                 .faculties(facultyMapper.mapToDto(subject.getFaculties())).build();
    }

    public Subject mapToEntity (SubjectDto subjectDto) {
        log.debug("Subject DTO before mapping: {}", subjectDto);
        return Subject.builder()
                .name(subjectDto.getName())
                .build();
    }

    public Set<SubjectDto> mapToDto (Set<Subject> subjects) {
        log.debug("Mapping a set of subjects to DTO");
        return subjects.stream().map(this::mapToDto).collect(Collectors.toSet());
    }
}
