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
        FacultyDTO facultyDTOMock = new FacultyDTO(1L, "TestName", 10, 20);
        Faculty mockFaculty = new Faculty(1L, "TestName", 10, 20);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(mockFaculty);
        Faculty faculty = facultyService.addFaculty(facultyDTOMock);
        Assertions.assertEquals("TestName", faculty.getName());
    }


}
