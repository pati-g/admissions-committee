package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.service.FacultyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static com.patrycjagalant.admissionscommittee.utils.Constants.FACULTY_DTO;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class FacultyControllerTests {
    private final String USER = "nr_one@test.com";
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private FacultyService facultyService;

    @Test
    @WithUserDetails(USER)
    void givenEditFacultyPage_whenNotAuthorized_thenAccessDenied() throws Exception {
        this.mockMvc.perform(get("/faculties/1/edit"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenEditFacultyPage_whenAuthorized_thenOK() throws Exception {
        this.mockMvc.perform(get("/faculties/1/edit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("faculties/editFaculty"))
                .andExpect(model().attribute(FACULTY_DTO, hasProperty("id", is(1L))))
                .andExpect(model().attributeExists("subjects"))
                .andExpect(model().attributeExists("allSubjects"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenEditFacultyPage_whenIncorrectID_ThenRedirect() throws Exception {
        this.mockMvc.perform(get("/faculties/notanumber/edit"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void listOfFacultiesOpensWithoutAuthentication() throws Exception {
        this.mockMvc.perform(get("/faculties"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void facultyViewOpensWithoutAuthentication() throws Exception {
        this.mockMvc.perform(get("/faculties/1"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenFacultyViewWithWrongId_ThenRedirect() throws Exception {
        this.mockMvc.perform(get("/faculties/165658698434"))
                .andDo(print())
//                .andExpect(model().attribute(ERROR, "Incorrect faculty ID, please try again."))
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/faculties/blabla"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenAddFacultyPage_whenAuthorized_thenOK() throws Exception {
        this.mockMvc.perform(get("/faculties/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute(FACULTY_DTO, new FacultyDto()));
    }

}
