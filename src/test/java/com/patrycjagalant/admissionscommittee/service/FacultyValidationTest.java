package com.patrycjagalant.admissionscommittee.service;

//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//class FacultyValidationTest {
//
//    @Mock
//    FacultyService facultyService;
//
//    @Mock
//    BindingResult result;
//
//    @InjectMocks
//    FacultyController facultyController;
//
//    @BeforeEach
//    public void setup() {
//        facultyController = new FacultyController(facultyService);
//        MockitoAnnotations.openMocks(this);
//        Mockito.when(result.hasErrors()).thenReturn(false);
//    }
//
////    @Test
////    void whenAddNewFaculty_And_GivenIncorrectInput_ThenThrowException() {
////        FacultyDto testDTO1 = new FacultyDto();
////        FacultyDto testDTO2 = new FacultyDto(null,"Psychology", null, null);
////        FacultyDto testDTO3 = new FacultyDto(null, null, 0, 10);
////
//////        assertThrows(MethodArgumentNotValidException.class, () -> facultyController.addFaculty(testDTO1, result));
//////        assertThrows(MethodArgumentNotValidException.class, () -> facultyController.addFaculty(testDTO2, result));
//////        assertThrows(MethodArgumentNotValidException.class, () -> facultyController.addFaculty(testDTO3, result));
////    }
//}
