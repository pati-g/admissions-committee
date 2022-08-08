package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {
    @Mock
    private FacultyRepository facultyRepository;
    private FacultyService facultyService;
    private final FacultyMapper facultyMapper = new FacultyMapper();

    @BeforeEach
    void setUp() {
        facultyService = new FacultyService(facultyRepository, facultyMapper);
    }

    @Test
    void testAddFacultyHappyPath(){
        FacultyDto facultyDtoMock = new FacultyDto( null, "TestName", 10, 20);
        Faculty mockFaculty = new Faculty(1L, "TestName", 10, 20);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(mockFaculty);

        FacultyDto faculty = facultyService.addFaculty(facultyDtoMock);

        assertEquals("TestName", faculty.getName());
    }

    @Test
    void testEditFacultyHappyPath(){
        FacultyDto facultyDtoMock = new FacultyDto(null, "ChangedName", null, 25);
        Faculty facultyMock = new Faculty(1L, "TestName", 10, 20);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(facultyMock);
        when(facultyRepository.getReferenceById(1L)).thenReturn(facultyMock);

        Faculty faculty = facultyService.editFaculty(facultyDtoMock, facultyMock.getId());

        assertEquals("ChangedName", faculty.getName());
        assertEquals(25, faculty.getTotalPlaces());
        assertEquals(10, faculty.getBudgetPlaces());
    }

}
