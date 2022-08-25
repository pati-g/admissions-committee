package com.patrycjagalant.admissionscommittee.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.patrycjagalant.admissionscommittee.controller.RequestController.INCORRECT_REQUEST;
import static com.patrycjagalant.admissionscommittee.utils.Constants.ALL_REQUESTS;
import static com.patrycjagalant.admissionscommittee.utils.Constants.ERROR;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenGetAllRequestsAsAdmin_ThenSuccess() throws Exception {
        mockMvc.perform(get("/request/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("requests/allRequests"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenGetStatementFormAndAuthorized_ThenOK() throws Exception {
        mockMvc.perform(get("/request/1010/statement"))
                .andExpect(status().isOk())
                .andExpect(view().name("requests/statement"))
                .andExpect(model().attributeExists("request"))
                .andExpect(model().attributeExists("applicant"))
                .andExpect(model().attributeExists("scores"))
                .andExpect(model().attributeExists("faculty"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenGetStatementFormAndWrongId_ThenErrorMessage() throws Exception {
        mockMvc.perform(get("/request/notvalid/statement"))
                .andExpect(status().isOk())
                .andExpect(view().name(ALL_REQUESTS))
                .andExpect(model().attribute(ERROR, INCORRECT_REQUEST));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenDeleteRequestAndWrongId_ThenErrorMessage() throws Exception {
        mockMvc.perform(delete("/request/notvalid/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute(ERROR, INCORRECT_REQUEST));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenDeleteRequestAndAuthorized_ThenOK() throws Exception {
        mockMvc.perform(delete("/request/1/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeDoesNotExist(ERROR));
    }
}
