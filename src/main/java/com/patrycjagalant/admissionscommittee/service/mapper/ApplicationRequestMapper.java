package com.patrycjagalant.admissionscommittee.service.mapper;


import com.patrycjagalant.admissionscommittee.dto.ApplicationRequestDTO;
import com.patrycjagalant.admissionscommittee.entity.ApplicationRequest;


public class ApplicationRequestMapper {

    public ApplicationRequestDTO mapToDto(ApplicationRequest applicationRequest) {
        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();
        ApplicantMapper applicantMapper = new ApplicantMapper();
        FacultyMapper facultyMapper = new FacultyMapper();

        applicationRequestDTO.setId(applicationRequest.getId());
        applicationRequestDTO.setRegistrationDate(applicationRequest.getRegistrationDate());
        applicationRequestDTO.setApplicant(applicantMapper.mapToDto(applicationRequest.getApplicant()));
        applicationRequestDTO.setFaculty(facultyMapper.mapToDto(applicationRequest.getFaculty()));
        return applicationRequestDTO;
    }

    public ApplicationRequest mapToEntity(ApplicationRequestDTO applicationRequestDTO) {
        ApplicationRequest applicationRequest = new ApplicationRequest();
        ApplicantMapper applicantMapper = new ApplicantMapper();
        FacultyMapper facultyMapper = new FacultyMapper();

        applicationRequest.setRegistrationDate(applicationRequestDTO.getRegistrationDate());
        applicationRequest.setApplicant(applicantMapper.mapToEntity(applicationRequestDTO.getApplicant()));
        applicationRequest.setFaculty(facultyMapper.mapToEntity(applicationRequestDTO.getFaculty()));
        return applicationRequest;
    }

    public void mapToEntity(ApplicationRequest applicationRequest, ApplicationRequestDTO applicationRequestDTO) {
        FacultyMapper facultyMapper = new FacultyMapper();
        ApplicantMapper applicantMapper = new ApplicantMapper();
        if (applicationRequestDTO.getApplicant() != null)
            applicationRequest.setApplicant(applicantMapper.mapToEntity(applicationRequestDTO.getApplicant()));
        if (applicationRequestDTO.getFaculty() != null)
            applicationRequest.setFaculty(facultyMapper.mapToEntity(applicationRequestDTO.getFaculty()));
    }
}
