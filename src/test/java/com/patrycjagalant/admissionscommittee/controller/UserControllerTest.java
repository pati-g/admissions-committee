package com.patrycjagalant.admissionscommittee.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static com.patrycjagalant.admissionscommittee.utils.Constants.USER_DTO;
import static com.patrycjagalant.admissionscommittee.utils.Constants.VIEW_PROFILE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final String USER = "nr_one@test.com";

    @Test
    @WithUserDetails(USER)
    void whenViewOwnAccount_ThenOK() throws Exception {
        this.mockMvc.perform(get("/user/one"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists(USER_DTO))
                .andExpect(view().name(VIEW_PROFILE));
    }

    @Test
    @WithUserDetails(USER)
    void whenViewOwnEditAccountForm_ThenOK() throws Exception {
        this.mockMvc.perform(get("/user/one/edit"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists(USER_DTO))
                .andExpect(view().name("editAccount"));
    }

    @Test
    @WithUserDetails(USER)
    void whenViewOtherAccount_ThenForbidden() throws Exception {
        this.mockMvc.perform(get("/user/two"))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(USER)
    void whenViewOtherEditAccountForm_ThenForbidden() throws Exception {
        this.mockMvc.perform(get("/user/two/edit"))
                .andDo(print()).andExpect(status().isForbidden());
    }

}
