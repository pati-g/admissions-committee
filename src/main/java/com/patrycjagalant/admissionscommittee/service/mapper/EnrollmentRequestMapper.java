package com.patrycjagalant.admissionscommittee.service.mapper;


import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EnrollmentRequestMapper {
    private final ApplicantMapper applicantMapper;

    private final FacultyMapper facultyMapper;

    public EnrollmentRequestMapper(ApplicantMapper applicantMapper, FacultyMapper facultyMapper) {
        this.applicantMapper = applicantMapper;
        this.facultyMapper = facultyMapper;
    }

    public EnrollmentRequestDto mapToDto(EnrollmentRequest enrollmentRequest) {
        log.debug("EnrollmentRequest entity before mapping: {}", enrollmentRequest);
        return EnrollmentRequestDto.builder()
                .id(enrollmentRequest.getId())
                .points(enrollmentRequest.getPoints())
                .registrationDate(enrollmentRequest.getRegistrationDate() != null ? enrollmentRequest.getRegistrationDate() : null)
                .status(enrollmentRequest.getStatus())
                .applicant(applicantMapper.mapToDtoWithoutRequests(enrollmentRequest.getApplicant()))
                .faculty(facultyMapper.mapToDto(enrollmentRequest.getFaculty())).build();
    }

    public List<EnrollmentRequestDto> mapToDto(List<EnrollmentRequest> enrollmentRequests) {
        log.debug("Mapping List<EnrollmentRequest> to DTOs");
        return enrollmentRequests.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public EnrollmentRequest mapToEntity(EnrollmentRequestDto enrollmentRequestDTO) {
        log.debug("EnrollmentRequest Dto before mapping: {}", enrollmentRequestDTO);
        return EnrollmentRequest.builder()
                .points(enrollmentRequestDTO.getPoints())
                .registrationDate(enrollmentRequestDTO.getRegistrationDate())
                .status(enrollmentRequestDTO.getStatus())
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
    }
}
