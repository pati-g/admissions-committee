//package com.patrycjagalant.admissionscommittee.integration;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.patrycjagalant.admissionscommittee.dto.UserDto;
//import com.patrycjagalant.admissionscommittee.entity.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.hamcrest.Matchers.hasProperty;
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class RegisterUserIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void registrationWorksInAllLayers() throws Exception {
//        UserDto user = UserDto.builder()
//                .username("MockMe")
//                .email("mock_me.2@gmail.com")
//                .password("password")
//                .matchingPassword("password")
//                .build();
//        mockMvc.perform(get("register")).andDo().
//
//        mockMvc.perform(post("/register")
//                        .contentType("application/json")
//                        .param("sendWelcomeMail", "true")
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(model().attribute("user", hasProperty("username", is("MockMe"))));
//    }
//}
