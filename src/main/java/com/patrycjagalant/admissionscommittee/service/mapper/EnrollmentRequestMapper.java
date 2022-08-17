package com.patrycjagalant.admissionscommittee.service.mapper;


import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EnrollmentRequestMapper {

    public EnrollmentRequestDto mapToDto(EnrollmentRequest enrollmentRequest) {
        log.debug("EnrollmentRequest entity before mapping: {}", enrollmentRequest);
ApplicantMapper applicantMapper = new ApplicantMapper();
 FacultyMapper facultyMapper = new FacultyMapper();
        return EnrollmentRequestDto.builder()
                .id(enrollmentRequest.getId())
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
ApplicantMapper applicantMapper = new ApplicantMapper();
FacultyMapper facultyMapper = new FacultyMapper();
        return EnrollmentRequest.builder()
                .registrationDate(enrollmentRequestDTO.getRegistrationDate())
                .status(enrollmentRequestDTO.getStatus())
                .applicant(applicantMapper.mapToEntityWithId(enrollmentRequestDTO.getApplicant()))
                .faculty(facultyMapper.mapToEntityWithId(enrollmentRequestDTO.getFaculty())).build();
    }

    public void mapToEntity(EnrollmentRequest enrollmentRequest, EnrollmentRequestDto enrollmentRequestDTO) {
        log.debug("EnrollmentRequest entity: {} and Dto: {} before mapping", enrollmentRequest, enrollmentRequestDTO);
        Status status = enrollmentRequestDTO.getStatus();
ApplicantMapper applicantMapper = new ApplicantMapper();
FacultyMapper facultyMapper = new FacultyMapper();
        if (status != null) {
            enrollmentRequest.setStatus(status);
        }
        if (enrollmentRequestDTO.getApplicant() != null) {
            enrollmentRequest.setApplicant(applicantMapper.mapToEntity(enrollmentRequestDTO.getApplicant()));
        }
        if (enrollmentRequestDTO.getFaculty() != null) {
            enrollmentRequest.setFaculty(facultyMapper.mapToEntity(enrollmentRequestDTO.getFaculty()));
        }
    }
}
