package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.SubjectDto;
import com.patrycjagalant.admissionscommittee.entity.Subject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SubjectMapper.class, FacultyMapper.class})
class SubjectMapperTests {
    @Autowired
    private SubjectMapper mapper;

    @Autowired
    private FacultyMapper facultyMapper;
    private Subject entity2;
    private Subject entity;

    @BeforeEach
    void setUp() {
        entity2 = new Subject(10L,  "Middle Earth History",null);
        entity = new Subject(80L, "Quantum physics", null);
    }

    @Test
    void mapToDtoMapsCorrectly() {
        SubjectDto dto2 = mapper.mapToDto(entity);
        Assertions.assertNotNull(dto2);
        assertEquals(new SubjectDto(80L, "Quantum physics", null), dto2);
    }


    @Test
    void mapToDtoListReturnsCorrectEntity() {
        Set<Subject> entities = Set.of(entity, entity2);
        Set<SubjectDto> dtos = mapper.mapToDto(entities);
        assertThat(entities).hasSize(2);
        assertThat(entities.stream().map(Subject::getName).collect(Collectors.toSet()))
                .isEqualTo(Set.of("Middle Earth History", "Quantum physics"));
    }
}
