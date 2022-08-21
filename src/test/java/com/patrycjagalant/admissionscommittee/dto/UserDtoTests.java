package com.patrycjagalant.admissionscommittee.dto;

import com.patrycjagalant.admissionscommittee.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTests {

    private UserDto userDto;

    @BeforeEach
    void setup() {
        userDto = new UserDto(1100L, "test_username", "test@user.com",
                "12345678", "12345678", false, true, Role.USER, null);
    }

    @Test
    void dtoInternalStateIsCorrect() {
        assertEquals(1100L, userDto.getId());
        assertEquals("test_username", userDto.getUsername());
        assertEquals(Role.USER, userDto.getRole());
    }

    @Test
    void twoIdenticalInstancesMatch() {
        assertThat(userDto).isEqualTo(new UserDto(1100L, "test_username", "test@user.com",
                "12345678", "12345678", false, true, Role.USER, null));
    }

    @Test
    void whenFieldIsDifferentInstancesDontMatch() {
        assertThat(userDto).isNotEqualTo(new UserDto(null, "test", "test@user.com",
                "12345678", "12345678", false, true, Role.USER, null))
                .isNotEqualTo(new UserDto(null, "test_username", "test@user.com",
                "12345678", "12345678", true, true, Role.USER, null))
                .isNotEqualTo(new UserDto(null, "test_username", "test2@user.com",
                "12345678", "12345678", false, true, Role.USER, null));
    }
}
