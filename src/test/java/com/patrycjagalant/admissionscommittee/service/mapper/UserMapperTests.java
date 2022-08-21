package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.dto.other.UserDtoForEditing;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserMapper.class)
class UserMapperTests {
    @Autowired
    private UserMapper mapper;

    private UserDto dto;
    private User entity;

    @BeforeEach
    void setUp() {
        dto = new UserDto(100L, "Frodo", "frodo@shire.com","Ih8sauron",
                "Ih8sauron", false, true, Role.USER, null);
        entity = new User(500L, "Samwise", "sam.is.wise@shire.gov",
                "potatoes", Role.USER, false, true);
    }

    @Test
    void mapDtoToEntityReturnsCorrectEntity() {
        User user = mapper.mapToEntity(dto);
        assertEquals("Frodo", user.getUsername());
        assertEquals("Ih8sauron", user.getPassword());
    }

    @Test
    void mapToDtoMapsCorrectly() {
        UserDto dto2 = mapper.mapToDto(entity);
        Assertions.assertNotNull(dto2);
        assertEquals(new UserDto(500L, "Samwise", "sam.is.wise@shire.gov",null,
                null, false, true, Role.USER, null), dto2);
    }

    @Test
    void whenMapDtoToEntityIfDtoValueIsNullThenEntityValueDoesntChange(){
        String newEmail = "not-so-wise@shire.gov";
        UserDtoForEditing dto2 = UserDtoForEditing.builder().email(newEmail).build();
        mapper.mapToEntity(dto2, entity);

        assertEquals("Samwise", entity.getUsername());
        assertEquals(newEmail, entity.getEmail());
        assertTrue(entity.isEnabled());
        assertNotNull(entity.getPassword());

        UserDtoForEditing emptyDto =
                new UserDtoForEditing();
        mapper.mapToEntity(emptyDto, entity);

        assertThat(entity).hasNoNullFieldsOrProperties();
    }
}
