package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;


import java.sql.Timestamp;

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

    @BeforeEach
    void setUp() {
        facultyService = new FacultyService(facultyRepository);
    }

    @Test
    void testAddFacultyHappyPath(){
        FacultyDTO facultyDTOMock = new FacultyDTO( "TestName", 10, 20);
        Faculty mockFaculty = new Faculty(1L, "TestName", 10, 20);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(mockFaculty);

        Faculty faculty = facultyService.addFaculty(facultyDTOMock);

        assertEquals("TestName", faculty.getName());
    }

    @Test
    void testEditFacultyHappyPath(){
        FacultyDTO facultyDTOMock = new FacultyDTO("ChangedName", null, 25);
        Faculty facultyMock = new Faculty(1L, "TestName", 10, 20);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(facultyMock);
        when(facultyRepository.getReferenceById(1L)).thenReturn(facultyMock);

        Faculty faculty = facultyService.editFaculty(facultyDTOMock, facultyMock.getId());

        assertEquals("ChangedName", faculty.getName());
        assertEquals(25, faculty.getTotalPlaces());
        assertEquals(10, faculty.getBudgetPlaces());
    }

}
