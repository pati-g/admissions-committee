package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.controller.FacultyController;
import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyValidationTest {

    @Mock
    FacultyService facultyService;

    @Mock
    BindingResult result;

    @InjectMocks
    FacultyController facultyController;

    @BeforeEach
    public void setup() {
        facultyController = new FacultyController(facultyService);
        MockitoAnnotations.openMocks(this);
        Mockito.when(result.hasErrors()).thenReturn(false);
    }

    @Test
    void whenAddNewFaculty_And_GivenIncorrectInput_ThenThrowException() {
        FacultyDTO testDTO1 = new FacultyDTO();
        FacultyDTO testDTO2 = new FacultyDTO("Psychology", null, null);
        FacultyDTO testDTO3 = new FacultyDTO(null, 0, 10);

        assertThrows(MethodArgumentNotValidException.class, () -> facultyController.addFaculty(testDTO1, result));
        assertThrows(MethodArgumentNotValidException.class, () -> facultyController.addFaculty(testDTO2, result));
        assertThrows(MethodArgumentNotValidException.class, () -> facultyController.addFaculty(testDTO3, result));
    }
}
