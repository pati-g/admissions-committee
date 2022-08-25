package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.patrycjagalant.admissionscommittee.utils.Constants.REGISTER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomepageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenOpenRegistrationForm_ThenSuccess() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", new UserDto()))
                .andExpect(view().name(REGISTER));
    }

    @Test
    void whenOpenHomePage_ThenSuccess() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void whenOpenLogin_ThenSuccess() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void whenOpenLogout_ThenSuccess() throws Exception {
        mockMvc.perform(get("/logout-success"))
                .andExpect(status().isOk())
                .andExpect(view().name("logout"));
    }
}
