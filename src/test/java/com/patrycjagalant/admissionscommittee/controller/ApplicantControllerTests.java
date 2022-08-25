package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import com.patrycjagalant.admissionscommittee.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicantControllerTests {

    private final String USER = "nr_one@test.com";
    @Mock
    Model model;
    @Autowired
    private ApplicantController controller;
    @Autowired
    private ApplicantService service;
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithUserDetails(USER)
    void givenAllApplicantsPage_whenNotAuthorized_thenForbidden() throws Exception {
        this.mockMvc.perform(get("/applicant/all"))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(USER)
    void userCanAccessOwnProfileView() throws Exception {
        this.mockMvc.perform(get("/applicant/one"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(model().size(6));
    }

    @Test
    @WithUserDetails(USER)
    void userCanAccessOwnProfileEditForm() throws Exception {
        this.mockMvc.perform(get("/applicant/one/edit"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists(APPLICANT_DTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenAdminRequestsListOfApplicants_ThenOK() throws Exception {
        this.mockMvc.perform(get("/applicant/all"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(model().size(6))
                .andExpect(view().name(ALL_APPLICANTS));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenAdminDeletesApplicant_ThenOK() throws Exception {
        this.mockMvc.perform(delete("/applicant/13/delete").with(csrf()))
                .andExpect(status().is3xxRedirection());
        assertThat(userService.findById(300L).isEnabled()).isFalse();
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotAccessOtherUsersProfileView() throws Exception {
        this.mockMvc.perform(get("/applicant/two"))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @WithUserDetails("nr_three@test.com")
    void whenSavedCertificateIsCorrect_ThenOk() {
        String name1 = "test-file.pdf";
        byte[] content = new byte[]{0, 1, 3, 4};
        MultipartFile file1 = new MockMultipartFile(name1, name1, "application/pdf", content);
        assertThat(service.getByUserId(300L).get().getCertificateUrl()).isNullOrEmpty();
        String returnView = controller.saveCertificate(file1, "three", model);
        assertThat(returnView).isEqualTo(REDIRECT_EDIT_APPLICANT);
        assertThat(service.getByUserId(300L).get().getCertificateUrl()).isNotNull().isNotEmpty();
    }

    @Test
    @Transactional
    @WithUserDetails("nr_three@test.com")
    void whenDownloadOwnCertificate_ThenOk() {
        String name1 = "test-file.pdf";
        byte[] content = new byte[]{0, 1, 3, 4};
        MultipartFile file1 = new MockMultipartFile(name1, name1, "application/pdf", content);
        controller.saveCertificate(file1, "three", model);
        ResponseEntity<Resource> downloadedFile = controller.downloadCertificate("three");
        assertThat(downloadedFile).isNotNull();
        assertThat(downloadedFile.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
