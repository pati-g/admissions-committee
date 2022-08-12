package com.patrycjagalant.admissionscommittee.service.mapper;


import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;

import java.util.List;
import java.util.stream.Collectors;


public class EnrollmentRequestMapper {

    public EnrollmentRequestDto mapToDto(EnrollmentRequest enrollmentRequest) {
        ApplicantMapper applicantMapper = new ApplicantMapper();
        FacultyMapper facultyMapper = new FacultyMapper();
        return EnrollmentRequestDto.builder()
                .id(enrollmentRequest.getId())
                .registrationDate(enrollmentRequest.getRegistrationDate() != null ? enrollmentRequest.getRegistrationDate() : null)
                .status(enrollmentRequest.getStatus())
                .applicant(applicantMapper.mapToDtoWithoutRelations(enrollmentRequest.getApplicant()))
                .faculty(facultyMapper.mapToDto(enrollmentRequest.getFaculty())).build();
    }

    public List<EnrollmentRequestDto> mapToDto(List<EnrollmentRequest> enrollmentRequests) {
        EnrollmentRequestMapper mapper = new EnrollmentRequestMapper();
        return enrollmentRequests.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public EnrollmentRequest mapToEntity(EnrollmentRequestDto enrollmentRequestDTO) {
        ApplicantMapper applicantMapper = new ApplicantMapper();
        FacultyMapper facultyMapper = new FacultyMapper();

        return EnrollmentRequest.builder()
                .registrationDate(enrollmentRequestDTO.getRegistrationDate())
                .status(enrollmentRequestDTO.getStatus())
                .applicant(applicantMapper.mapToEntity(enrollmentRequestDTO.getApplicant()))
                .faculty(facultyMapper.mapToEntity(enrollmentRequestDTO.getFaculty())).build();
    }

    public void mapToEntity(EnrollmentRequest enrollmentRequest, EnrollmentRequestDto enrollmentRequestDTO) {
        FacultyMapper facultyMapper = new FacultyMapper();
        ApplicantMapper applicantMapper = new ApplicantMapper();
        Character status = enrollmentRequestDTO.getStatus();
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
