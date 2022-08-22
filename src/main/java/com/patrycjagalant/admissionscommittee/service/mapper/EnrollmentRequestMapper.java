package com.patrycjagalant.admissionscommittee.service.mapper;


import com.patrycjagalant.admissionscommittee.annotations.Mapper;
import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.dto.other.RequestWithNamesDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.entity.Status;
import com.patrycjagalant.admissionscommittee.service.ScoreService;
import com.patrycjagalant.admissionscommittee.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Mapper
public class EnrollmentRequestMapper {
    private final ApplicantMapper applicantMapper;
    private final FacultyMapper facultyMapper;
    private final ScoreService scoreService;
    private final SubjectService subjectService;

    public EnrollmentRequestDto mapToDto(EnrollmentRequest enrollmentRequest) {
        log.debug("EnrollmentRequest entity before mapping: {}", enrollmentRequest);
        ApplicantDto applicantDto = applicantMapper.mapToDto(enrollmentRequest.getApplicant());
        applicantDto.setScores(scoreService.getAllForApplicantId(applicantDto.getId()));
        FacultyDto facultyDto = facultyMapper.mapToDto(enrollmentRequest.getFaculty());
        facultyDto.setSubjects(subjectService.getAllForFaculty(facultyDto.getId()));
        return EnrollmentRequestDto.builder()
                .id(enrollmentRequest.getId())
                .points(enrollmentRequest.getPoints())
                .registrationDate(enrollmentRequest.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
                .status(enrollmentRequest.getStatus())
                .tempStatus(enrollmentRequest.getTempStatus())
                .applicant(applicantDto)
                .faculty(facultyDto).build();
    }

    public List<EnrollmentRequestDto> mapToDto(List<EnrollmentRequest> enrollmentRequests) {
        log.debug("Mapping List<EnrollmentRequest> to DTOs");
        return enrollmentRequests.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public EnrollmentRequest mapToEntity(EnrollmentRequestDto enrollmentRequestDTO) {
        log.debug("EnrollmentRequest Dto before mapping: {}", enrollmentRequestDTO);
        return EnrollmentRequest.builder()
                .points(enrollmentRequestDTO.getPoints())
                .status(enrollmentRequestDTO.getStatus())
                .tempStatus(enrollmentRequestDTO.getTempStatus())
                .applicant(applicantMapper.mapToEntityWithId(enrollmentRequestDTO.getApplicant()))
                .faculty(facultyMapper.mapToEntityWithId(enrollmentRequestDTO.getFaculty())).build();
    }

    public void mapToEntity(EnrollmentRequest enrollmentRequest, EnrollmentRequestDto enrollmentRequestDTO) {
        log.debug("EnrollmentRequest entity: {} and Dto: {} before mapping", enrollmentRequest, enrollmentRequestDTO);
        Status status = enrollmentRequestDTO.getStatus();
        if (status != null) {
            enrollmentRequest.setStatus(status);
        }
        if (enrollmentRequestDTO.getApplicant() != null) {
            enrollmentRequest.setApplicant(applicantMapper.mapToEntity(enrollmentRequestDTO.getApplicant()));
        }
        if (enrollmentRequestDTO.getFaculty() != null) {
            enrollmentRequest.setFaculty(facultyMapper.mapToEntity(enrollmentRequestDTO.getFaculty()));
        }
        if (enrollmentRequestDTO.getPoints() != null) {
            enrollmentRequest.setPoints(enrollmentRequestDTO.getPoints());
        }
        if(!enrollmentRequestDTO.getTempStatus().equals(Status.PENDING)) {
            enrollmentRequest.setTempStatus(enrollmentRequestDTO.getTempStatus());
        }
    }

    public RequestWithNamesDto mapToDtoWithNames(EnrollmentRequest request) {
        Applicant applicant = request.getApplicant();
        Faculty faculty = request.getFaculty();
        return RequestWithNamesDto.builder()
                .faculty(faculty.getName())
                .applicant(applicant.getLastName() + " " + applicant.getFirstName())
                .id(request.getId())
                .points(request.getPoints())
                .date(request.getRegistrationDate())
                .status(request.getStatus())
                .build();
    }
}
