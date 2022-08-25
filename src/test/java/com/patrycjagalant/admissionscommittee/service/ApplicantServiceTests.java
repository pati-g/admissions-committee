package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchUserException;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ApplicantService.class)
class ApplicantServiceTests {

    @Mock
    private ApplicantRepository repository;
    private ApplicantService service;
    @Mock
    private ApplicantMapper mapper;
    @Mock
    private ScoreService scoreService;
    @Mock
    private EnrollmentRequestService requestService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private ApplicantDto dto;
    private Applicant applicant;

    @BeforeEach
    void setUp() {
        this.service = new ApplicantService(repository, mapper,
                scoreService, requestService, userService, userRepository);
        dto = new ApplicantDto(100L, null, "Frodo", "Baggins", "Shire",
                "Middle Earth", "not specified",
                null, null, null, null);
        applicant = new Applicant(100L, "Frodo", "Baggins", "Shire",
                "Middle Earth", "not specified",
                null, null, null, null);
    }

    @Test
    void whenAddingNewApplicant_ThenSuccess() {
        Long userId = 123L;
        Mockito.when(repository.findByUserId(userId)).thenReturn(Optional.empty());
        Mockito.when(mapper.mapToEntity(eq(dto), any(Applicant.class))).thenReturn(applicant);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        Mockito.when(repository.save(applicant)).thenReturn(applicant);

        assertThat(service.addNewOrEditApplicant(dto, userId))
                .isInstanceOf(Applicant.class)
                .matches(applicant1 -> (applicant1.getLastName().equalsIgnoreCase(dto.getLastName()) &&
                        applicant1.getRegion().equalsIgnoreCase(dto.getRegion())));
    }

    @Test
    void whenUpdatingApplicantWithWrongUserId_ThenExceptionIsThrown() {
        Long userId = 123L;
        Mockito.when(repository.findByUserId(userId))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchUserException.class)
                .isThrownBy(() -> service.addNewOrEditApplicant(dto, userId))
                .withMessage("User could not be found.");
    }

    @Test
    void whenUpdatingApplicant_ThenSuccess() {
        Long userId = 123L;
        String firstName = "Samwise";
        String lastName = "Gamgee";
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        Applicant newApplicant = Applicant.builder().firstName(firstName).lastName(lastName).build();
        Mockito.when(repository.findByUserId(userId)).thenReturn(Optional.ofNullable(applicant));
        Mockito.when(mapper.mapToEntity(dto, applicant)).thenReturn(newApplicant);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        Mockito.when(repository.save(newApplicant)).thenReturn(newApplicant);

        assertThat(service.addNewOrEditApplicant(dto, userId))
                .isInstanceOf(Applicant.class)
                .matches(applicant1 -> (applicant1.getLastName().equalsIgnoreCase(dto.getLastName()) &&
                        applicant1.getFirstName().equalsIgnoreCase(dto.getFirstName())));
    }

    // TODO: test saving file and downloading file
}
