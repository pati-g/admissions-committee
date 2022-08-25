package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.repository.EnrollmentRequestRepository;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.EnrollmentRequestMapper;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = FacultyService.class)
class FacultyServiceTests {
    @Mock
    private FacultyRepository facultyRepository;
    private FacultyService facultyService;
    @Mock
    private FacultyMapper facultyMapper;
    @Mock
    private ApplicantService applicantService;
    @Mock
    private EnrollmentRequestMapper requestMapper;
    @Mock
    private EnrollmentRequestService requestService;
    @Mock
    private EnrollmentRequestRepository requestRepository;
    @Mock
    private SubjectService subjectService;
    private FacultyDto facultyDtoMock;
    private Faculty mockFaculty;

    @BeforeEach
    void setUp() {
        facultyService = new FacultyService(facultyRepository, facultyMapper, applicantService,
                requestMapper, requestService, subjectService, requestRepository);
        facultyDtoMock =
                new FacultyDto(1L, "TestName", 10, 20, null, null);
        mockFaculty =
                new Faculty(1L, "TestName", 10, 20, null, null);
    }

    @Test
    void whenAddFaculty_ThenSuccess() {
        FacultyDto newDto = FacultyDto.builder().name("TestName").budgetPlaces(10).totalPlaces(20).build();
        Mockito.when(facultyMapper.mapToEntity(newDto)).thenReturn(mockFaculty);
        Mockito.when(facultyRepository.save(mockFaculty)).thenReturn(mockFaculty);
        Mockito.when(facultyMapper.mapToDto(mockFaculty)).thenReturn(facultyDtoMock);
        FacultyDto returnDto = facultyService.addFaculty(newDto);

        assertEquals(facultyDtoMock, returnDto);
    }

    @Test
    void whenEditFaculty_ThenSuccess() throws NoSuchFacultyException {
        String changedName = "ChangedName";
        facultyDtoMock.setName(changedName);
        facultyDtoMock.setTotalPlaces(25);
        Faculty newFaculty = Faculty.builder().name(facultyDtoMock.getName())
                .totalPlaces(facultyDtoMock.getTotalPlaces()).budgetPlaces(mockFaculty.getBudgetPlaces()).build();

        Mockito.when(facultyRepository.findById(1L)).thenReturn(Optional.ofNullable(mockFaculty));
        Mockito.when(facultyRepository.save(any(Faculty.class))).thenReturn(newFaculty);

        Faculty faculty = facultyService.editFaculty(facultyDtoMock, mockFaculty.getId());

        assertEquals("ChangedName", faculty.getName());
        assertEquals(25, faculty.getTotalPlaces());
        assertEquals(10, faculty.getBudgetPlaces());
    }


}
