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
    void whenFieldIsDifferent_ThenInstancesDontMatch() {
        assertThat(userDto).isNotEqualTo(new UserDto(null, "test", "test@user.com",
                "12345678", "12345678", false, true, Role.USER, null))
                .isNotEqualTo(new UserDto(null, "test_username", "test@user.com",
                "12345678", "12345678", true, true, Role.USER, null))
                .isNotEqualTo(new UserDto(null, "test_username", "test2@user.com",
                "12345678", "12345678", false, true, Role.USER, null));
    }

    @Test
    void whenSetFieldValue_ThenDtoValeIsChangedCorrectly() {
        userDto.setUsername("new-name");
        userDto.setEmail("new.email@user.com");
        userDto.setPassword("0987654321");
        userDto.setMatchingPassword("0987654321");
        userDto.setBlocked(true);
        userDto.setEnabled(false);
        userDto.setRole(Role.ADMIN);
        userDto.setApplicant(new ApplicantDto());

        assertThat(userDto.getRole()).isEqualTo(Role.ADMIN);
        assertThat(userDto.getEmail()).isEqualTo("new.email@user.com");
        assertThat(userDto.getUsername()).isEqualTo("new-name");
        assertThat(userDto.getPassword()).isEqualTo("0987654321");
        assertThat(userDto.getMatchingPassword()).isEqualTo("0987654321");
        assertThat(userDto.isBlocked()).isTrue();
        assertThat(userDto.isEnabled()).isFalse();
        assertThat(userDto.getApplicant()).isNotNull();
    }
}
