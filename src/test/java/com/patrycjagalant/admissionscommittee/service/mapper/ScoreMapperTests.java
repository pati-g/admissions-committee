package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.dto.other.UserDtoForEditing;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.Role;
import com.patrycjagalant.admissionscommittee.entity.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ScoreMapper.class)
class ScoreMapperTests {
    @Autowired
    private ScoreMapper mapper;

    private ScoreDto dto;
    private Score entity;

    @BeforeEach
    void setUp() {
        dto = new ScoreDto(10L, 13L, "Middle Earth History",99);
        entity = new Score(80L, new Applicant(), "Quantum physics", 67);
    }

    @Test
    void mapDtoToEntityReturnsCorrectEntity() {
        Score score = mapper.mapToEntity(dto);
        assertEquals("Middle Earth History", score.getSubjectName());
        assertEquals(99, score.getResult());
    }

    @Test
    void mapToDtoMapsCorrectly() {
        ScoreDto dto2 = mapper.mapToDto(entity);
        Assertions.assertNotNull(dto2);
        assertEquals(new ScoreDto(80L, null, "Quantum physics", 67), dto2);
    }

    @Test
    void whenMapDtoToEntityIfDtoValueIsNullThenEntityValueDoesntChange(){
        ScoreDto dto2 = ScoreDto.builder().result(50).build();
        mapper.mapToEntity(entity, dto2);

        assertEquals(50, entity.getResult());
        assertEquals("Quantum physics", entity.getSubjectName());
        assertNotNull(entity.getId());

        ScoreDto emptyDto =
                new ScoreDto();
        mapper.mapToEntity(entity, emptyDto);

        assertThat(entity).hasNoNullFieldsOrPropertiesExcept();
    }
}
