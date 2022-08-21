package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.dto.other.UserDtoForEditing;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.Role;
import com.patrycjagalant.admissionscommittee.entity.User;
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
@ContextConfiguration(classes = {ApplicantMapper.class, UserMapper.class})
class ApplicantMapperTests {

    @Autowired
    private ApplicantMapper mapper;

    @Autowired
    private UserMapper userMapper;
    private ApplicantDto dto;
    private Applicant entity;

    @BeforeEach
    void setUp() {
        dto = new ApplicantDto(100L, null, "Frodo", "Baggins", "Shire",
                "Middle Earth", "not specified", null, null, null, null);
        entity = new Applicant(500L, "Samwise", "Gamgee",
                "Shire", "Middle Earth", "not specified", null, null, null, null);
    }

    @Test
    void correctlyMapsFromDtoToEntityAndBack() {
        Applicant applicant = mapper.mapToEntity(dto);
        applicant.setId(100L);
        assertEquals("Frodo", applicant.getFirstName());
        assertEquals("Shire", applicant.getCity());
        ApplicantDto newDto = mapper.mapToDto(applicant);
        assertEquals("Frodo Baggins", newDto.getFullName());
        assertEquals("Shire", newDto.getCity());
    }

    @Test
    void mapToDtoMapsCorrectly() {
        ApplicantDto dto2 = mapper.mapToDto(entity);
        Assertions.assertNotNull(dto2);
        assertEquals(new ApplicantDto(500L, "Samwise Gamgee", "Samwise", "Gamgee",
                "Shire", "Middle Earth", "not specified", null, null, null, null), dto2);
    }

    @Test
    void whenMapDtoToEntityIfDtoValueIsNullThenEntityValueDoesntChange(){
        String newCity = "Mordor";
        ApplicantDto dto2 = ApplicantDto.builder().city(newCity).build();
        mapper.mapToEntity(dto2, entity);

        assertEquals("Samwise", entity.getFirstName());
        assertEquals(newCity, entity.getCity());
        assertNotNull(entity.getEducationalInstitution());
        assertNotNull(entity.getId());

        ApplicantDto emptyDto =
                new ApplicantDto();
        mapper.mapToEntity(emptyDto, entity);

        assertThat(entity.getFirstName()).isNotNull();
    }
}
