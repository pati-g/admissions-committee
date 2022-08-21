package com.patrycjagalant.admissionscommittee.service.mapper;
//
//import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
//import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
//import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
//import com.patrycjagalant.admissionscommittee.entity.Applicant;
//import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
//import com.patrycjagalant.admissionscommittee.entity.Faculty;
//import com.patrycjagalant.admissionscommittee.entity.Status;
//import com.patrycjagalant.admissionscommittee.service.ScoreService;
//import com.patrycjagalant.admissionscommittee.service.SubjectService;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith({SpringExtension.class, MockitoExtension.class})
//@ContextConfiguration(classes = {EnrollmentRequestMapper.class,
//        FacultyMapper.class, ScoreService.class, SubjectService.class})
//class EnrollmentRequestMapperTests {
//    @Autowired
//    private EnrollmentRequestMapper mapper;
//
//    @Mock
//    private ApplicantMapper applicantMapper;
//
//    @Autowired
//    private  FacultyMapper facultyMapper;
//
//    @Autowired
//    private ScoreService scoreService;
//
//    @Autowired
//    private SubjectService subjectService;
//    private EnrollmentRequestDto dto;
//    private EnrollmentRequest entity;
//
//
//    @BeforeEach
//    void setUp(){
//        dto = new EnrollmentRequestDto(345L, new ApplicantDto(), new FacultyDto(), "11.11.2011 11:11:11",
//                75, Status.PENDING, Status.REJECTED);
//        entity = new EnrollmentRequest(345L, new Applicant(), new Faculty(),
//                LocalDateTime.of(2011, 11, 11, 11, 11, 11),
//                75, Status.PENDING, Status.CONTRACT);
//    }
//    @Test
//    void mapDtoToEntityReturnsCorrectEntity() {
//        EnrollmentRequest request = mapper.mapToEntity(dto);
//
//        assertEquals(Status.PENDING, request.getStatus());
//        assertEquals(Status.REJECTED, request.getTempStatus());
//        assertEquals("11.11.2011 11:11:11",
//                request.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
//        assertEquals(75, request.getPoints());
//    }
//
//    @Test
//    void mapToDtoMapsCorrectly() {
//        EnrollmentRequestDto mappedDto = mapper.mapToDto(entity);
//
//        assertNotNull(mappedDto);
//        assertEquals(Status.PENDING, mappedDto.getStatus());
//        assertEquals(Status.CONTRACT, mappedDto.getTempStatus());
//        assertEquals("11.11.2011 11:11:11",
//                mappedDto.getRegistrationDate());
//    }
//
//    @Test
//    void mapperCanDoMultipleMappingsCorrectly() {
//        EnrollmentRequest testEntity2 = new EnrollmentRequest(10L, new Applicant(), new Faculty(),
//                LocalDateTime.now(),70, Status.REJECTED, Status.BUDGET);
//        EnrollmentRequest testEntity3 = new EnrollmentRequest(2222L, new Applicant(), new Faculty(),
//                LocalDateTime.now(),79, Status.CONTRACT, Status.REJECTED);
//
//        EnrollmentRequestDto dto2 = mapper.mapToDto(testEntity2);
//        EnrollmentRequestDto dto3 = mapper.mapToDto(testEntity3);
//
//        EnrollmentRequest result3 = mapper.mapToEntity(dto3);
//
//        // first batch of tests
//        assertNotNull(result3);
//        assertEquals(result3, testEntity3);
//        assertNull(result3.getId());
//
//        assertEquals(Status.BUDGET, dto2.getTempStatus());
//        assertEquals(79, dto3.getPoints());
//        assertEquals(testEntity2.getTempStatus(), dto2.getTempStatus());
//
//        mapper.mapToEntity(entity, dto3);
//
//        assertEquals(testEntity3, entity);
//    }
//
//    @Test
//    void whenMapDtoToEntityIfDtoValueIsNullThenEntityValueDoesntChange(){
//        EnrollmentRequestDto dto2 = EnrollmentRequestDto.builder().tempStatus(Status.REJECTED).points(20).build();
//        EnrollmentRequest entity2 = EnrollmentRequest.builder()
//                .id(10L)
//                .points(-1)
//                .faculty(new Faculty())
//                .applicant(new Applicant())
//                .status(Status.PENDING)
//                .registrationDate(LocalDateTime.now())
//                .build();
//        mapper.mapToEntity(entity2, dto2);
//
//        assertEquals(Status.REJECTED, entity2.getTempStatus());
//        assertEquals(20, entity2.getPoints());
//        assertNotNull(entity2.getFaculty());
//        assertNotNull(entity2.getRegistrationDate());
//
//        EnrollmentRequestDto dto3 = EnrollmentRequestDto.builder().points(60).build();
//        mapper.mapToEntity(entity2, dto3);
//
//        assertEquals(Status.REJECTED, entity2.getTempStatus());
//        assertEquals(60, entity2.getPoints());
//        assertEquals(Status.PENDING, entity2.getStatus());
//        assertNotNull(entity2.getRegistrationDate());
//    }
//}
