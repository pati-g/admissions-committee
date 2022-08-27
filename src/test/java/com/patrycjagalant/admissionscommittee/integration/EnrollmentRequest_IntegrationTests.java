package com.patrycjagalant.admissionscommittee.integration;

import com.patrycjagalant.admissionscommittee.controller.FacultyController;
import com.patrycjagalant.admissionscommittee.controller.RequestController;
import com.patrycjagalant.admissionscommittee.dto.*;
import com.patrycjagalant.admissionscommittee.entity.Status;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import com.patrycjagalant.admissionscommittee.service.EnrollmentRequestService;
import com.patrycjagalant.admissionscommittee.service.FacultyService;
import com.patrycjagalant.admissionscommittee.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.patrycjagalant.admissionscommittee.utils.Constants.REDIRECT_FACULTIES;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentRequestIntegrationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private EnrollmentRequestService requestService;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private RequestController requestController;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAttributes;
    @Autowired
    private SubjectService subjectService;

    @Test
    @Transactional
    void newRequestShouldBindApplicantAndFacultyCorrectly() {
        String facultyId = "200";
        Long userId = 1510L;
        User user = userRepository.findById(userId).orElseThrow();
        ApplicantDto applicantBefore = applicantService.getByUserId(userId).orElseThrow();
        assertThat(applicantBefore.getRequests()).isEmpty();
        facultyController.newRequest(facultyId, user, redirectAttributes);
        List<EnrollmentRequestDto> request = facultyService.getById(200L).getRequests().stream()
                .filter(requestDto ->
                        requestDto.getApplicant().getId() == (applicantBefore.getId()))
                .collect(Collectors.toList());

        ApplicantDto applicantAfter = applicantService.getByUserId(userId).orElseThrow();
        assertThat(request).hasSize(1);
        assertThat(requestService.getRequestById(request.get(0).getId())).isNotNull();
        assertThat(applicantAfter.getRequests()).isNotEmpty();
    }

    @Test
    void whenPostingNewRequest_IfUserHasNoApplicantProfile_ThenRedirect() {
        String facultyId = "200";
        Long userId = 1111L; // admin ID
        User user = userRepository.findById(userId).orElseThrow();
        String returnUrl = facultyController.newRequest(facultyId, user, redirectAttributes);
        assertThat(returnUrl).isEqualTo(REDIRECT_FACULTIES);
    }

    @Test
    @Transactional
    void whenPostingNewRequest_IfRequestExists_ThenRedirect() {
        String facultyId = "100";
        Long userId = 100L;
        User user = userRepository.findById(userId).orElseThrow();
        facultyController.newRequest(facultyId, user, redirectAttributes); // add non-existing request
        String returnUrl = facultyController.newRequest(facultyId, user, redirectAttributes); //try to add 2nd time
        assertThat(returnUrl).isEqualTo(REDIRECT_FACULTIES);
    }

    @Test
    @Transactional
    void submittingStatementChangesRequestStatusCorrectly() {
        Long requestId = 10L;
        requestController.submitStatement(String.valueOf(requestId), Status.BUDGET, model);
        assertThat(requestService.getRequestById(requestId).getStatus()).isEqualTo(Status.BUDGET);
    }

    @Test
    @Transactional
    void getRelevantScoresReturnsCorrectList() {
        ApplicantDto applicantDto = applicantService.getByUserId(100L).orElseThrow();
        FacultyDto facultyDto = facultyService.getById(200L);
        List<ScoreDto> scores = requestService.getRelevantScoresForApplicant(applicantDto, facultyDto);
        Set<SubjectDto> subjectDtos = subjectService.getAllForFaculty(200L);
        assertThat(scores.stream()
                .map(ScoreDto::getSubjectName)
                .collect(Collectors.toList()))
                .containsExactlyInAnyOrderElementsOf(subjectDtos.stream()
                        .map(SubjectDto::getName)
                        .collect(Collectors.toList()));
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void givenAddSubjectFaculty_whenAuthorized_thenSuccess() {
        FacultyDto dto = facultyService.getById(100L);
        assertThat(dto.getSubjects().stream().map(SubjectDto::getName)
                .collect(Collectors.toList())).isNotEmpty().doesNotContain("Biology");
        facultyController.addSubjectToList("6", "100", model);
        dto = facultyService.getById(100L);
        assertThat(dto.getSubjects().stream().map(SubjectDto::getName)
                .collect(Collectors.toList())).contains("Biology");
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void givenAddNewSubjectFaculty_whenAuthorized_thenSuccess() {
        Set<SubjectDto> subjects = subjectService.getAll();
        assertThat(subjects.stream().map(SubjectDto::getName)
                .collect(Collectors.toList())).isNotEmpty().doesNotContain("French");
        facultyController.createNewSubjectAndAddToList("French", "100", model);
        FacultyDto dto = facultyService.getById(100L);
        subjects = subjectService.getAll();
        assertThat(subjects.stream().map(SubjectDto::getName)
                .collect(Collectors.toList())).isNotEmpty().contains("French");
        assertThat(dto.getSubjects().stream().map(SubjectDto::getName)
                .collect(Collectors.toList())).contains("French");
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void whenDeleteSubjectFromFacultyAndAuthorized_thenSuccess() {
        FacultyDto dto = facultyService.getById(100L);
        assertThat(dto.getSubjects().stream().map(SubjectDto::getName)
                .collect(Collectors.toList())).contains("Polish");
        facultyController.deleteSubjectFromList("100", "1", model);
        dto = facultyService.getById(100L);
        assertThat(dto.getSubjects().stream().map(SubjectDto::getName)
                .collect(Collectors.toList())).isNotEmpty().doesNotContain("Polish");
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void whenProcessRequestAndAuthorized_thenCorrectRequestStatus() {
        facultyController.processRequests("200", redirectAttributes); // requests > total places
        facultyController.processRequests("400", redirectAttributes); // total > requests > budget
        facultyController.processRequests("100", redirectAttributes); // requests < budget
        List<EnrollmentRequestDto> requestsLessThanBudget = facultyService.getById(100L).getRequests();
        List<EnrollmentRequestDto> requestsLessThanTotal = facultyService.getById(400L).getRequests();
        List<EnrollmentRequestDto> requestsMoreThanTotal = facultyService.getById(200L).getRequests();
        requestsLessThanBudget.forEach(request -> assertThat(request.getTempStatus()).isEqualTo(Status.BUDGET));
        requestsLessThanTotal.forEach(request -> {
            assertThat(request.getTempStatus()).isNotEqualTo(Status.PENDING);
            assertThat(request.getTempStatus()).isNotEqualTo(Status.REJECTED);
        });
        requestsMoreThanTotal.forEach(request -> assertThat(request.getTempStatus()).isNotEqualTo(Status.PENDING));
    }
}
