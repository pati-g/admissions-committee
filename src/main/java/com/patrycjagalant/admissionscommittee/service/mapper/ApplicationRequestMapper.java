package com.patrycjagalant.admissionscommittee.service.mapper;


import com.patrycjagalant.admissionscommittee.dto.ApplicationRequestDto;
import com.patrycjagalant.admissionscommittee.entity.ApplicationRequest;
import java.util.Set;
import java.util.stream.Collectors;


public class ApplicationRequestMapper {

    public ApplicationRequestDto mapToDto(ApplicationRequest applicationRequest) {
        ApplicantMapper applicantMapper = new ApplicantMapper();
        FacultyMapper facultyMapper = new FacultyMapper();
        return ApplicationRequestDto.builder()
                .id(applicationRequest.getId())
                .registrationDate(applicationRequest.getRegistrationDate())
                .applicant(applicantMapper.mapToDto(applicationRequest.getApplicant()))
                .faculty(facultyMapper.mapToDto(applicationRequest.getFaculty())).build();
    }

    public Set<ApplicationRequestDto> mapToDto(Set<ApplicationRequest> applicationRequests) {
        ApplicationRequestMapper mapper = new ApplicationRequestMapper();
        return applicationRequests.stream().map(mapper::mapToDto).collect(Collectors.toSet());
    }

    public ApplicationRequest mapToEntity(ApplicationRequestDto applicationRequestDTO) {
        ApplicantMapper applicantMapper = new ApplicantMapper();
        FacultyMapper facultyMapper = new FacultyMapper();

        return ApplicationRequest.builder()
                .registrationDate(applicationRequestDTO.getRegistrationDate())
                .applicant(applicantMapper.mapToEntity(applicationRequestDTO.getApplicant()))
                .faculty(facultyMapper.mapToEntity(applicationRequestDTO.getFaculty())).build();
    }

    public void mapToEntity(ApplicationRequest applicationRequest, ApplicationRequestDto applicationRequestDTO) {
        FacultyMapper facultyMapper = new FacultyMapper();
        ApplicantMapper applicantMapper = new ApplicantMapper();
        if (applicationRequestDTO.getApplicant() != null)
            applicationRequest.setApplicant(applicantMapper.mapToEntity(applicationRequestDTO.getApplicant()));
        if (applicationRequestDTO.getFaculty() != null)
            applicationRequest.setFaculty(facultyMapper.mapToEntity(applicationRequestDTO.getFaculty()));
    }
}
