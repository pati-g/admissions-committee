package com.patrycjagalant.admissionscommittee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicantControllerTests {

    private final String USER = "nr_one@test.com";
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(USER)
    void givenAllApplicantsPage_whenNotAuthorized_thenAccessDenied() throws Exception {
        this.mockMvc.perform(get("/applicant/all"))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(USER)
    void userCanAccessTheirOwnProfileView() throws Exception {
        this.mockMvc.perform(get("/applicant/one"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(USER)
    void userCannotAccessOtherUsersProfileView() throws Exception {
        this.mockMvc.perform(get("/applicant/two"))
                .andDo(print()).andExpect(status().isForbidden());
    }

//    @Test
//    @WithUserDetails(USER)
//    void whenUserEditsProfileThenResponseRedirect() throws Exception {
//        ApplicantDto dto = ApplicantDto.builder().city("Gdańsk").build();
//        this.mockMvc.perform(put("/applicant/one/edit").requestAttr("applicantDTO", dto))
//                .andExpect(status().is3xxRedirection());
//    }
}
