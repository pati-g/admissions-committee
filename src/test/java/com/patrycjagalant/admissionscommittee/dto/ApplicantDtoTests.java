package com.patrycjagalant.admissionscommittee.dto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicantDtoTests {

    private ApplicantDto applicantDto;

    @BeforeEach
    void setup() {
        applicantDto = new ApplicantDto(500L, "Samwise Gamgee", "Samwise", "Gamgee",
                "Shire", "Middle Earth", "not specified", null, null,
                null, null);
    }

    @Test
    void dtoInternalStateIsCorrect() {
        assertEquals(500L, applicantDto.getId());
        assertEquals("Samwise Gamgee", applicantDto.getFullName());
        assertEquals("Middle Earth", applicantDto.getRegion());
    }

    @Test
    void twoIdenticalInstancesMatch() {
        assertThat(applicantDto).isEqualTo(new ApplicantDto(500L, "Samwise Gamgee", "Samwise", "Gamgee",
                "Shire", "Middle Earth", "not specified", null, null,
                null, null))
                .isEqualTo(new ApplicantDto(500L, null, "Samwise", "Gamgee",
                "Shire", "Middle Earth", "not specified", null, null,
                null, null));
    }

    @Test
    void whenFieldIsDifferent_ThenInstancesDontMatch() {
        assertThat(applicantDto).isNotEqualTo(new ApplicantDto(500L, "Samwise Gamgee", "Frodo", "Baggins",
                        "Shire", "Middle Earth", "not specified", null, null,
                        null, null))
                .isNotEqualTo(new ApplicantDto(500L, "Samwise Gamgee", "Samwise", "Gamgee",
                        "Rivendell", "Middle Earth", "not specified", null, null,
                        null, null));
    }

    @Test
    void whenSetFieldValue_ThenDtoValeIsChangedCorrectly() {
        applicantDto.setFullName("Frodo Baggins");
        applicantDto.setFirstName("Frodo");
        applicantDto.setLastName("Baggins");
        applicantDto.setCity("New Hampshire");
        applicantDto.setRegion("Hobbiton");
        applicantDto.setEducationalInstitution("Bilbo Academy");
        applicantDto.setScores(new ArrayList<>());
        applicantDto.setRequests(new ArrayList<>());
        applicantDto.setUserDetails(new UserDto());

        assertThat(applicantDto.getFullName()).isEqualTo("Frodo Baggins");
        assertThat(applicantDto.getFirstName()).isEqualTo("Frodo");
        assertThat( applicantDto.getLastName()).isEqualTo("Baggins");
        assertThat(applicantDto.getCity()).isEqualTo("New Hampshire");
        assertThat(applicantDto.getRegion()).isEqualTo("Hobbiton");
        assertThat(applicantDto.getEducationalInstitution()).isEqualTo("Bilbo Academy");
        assertThat(applicantDto.getScores()).isNotNull();
        assertThat(applicantDto.getRequests()).isNotNull();
        assertThat(applicantDto.getUserDetails()).isNotNull();
    }
}
