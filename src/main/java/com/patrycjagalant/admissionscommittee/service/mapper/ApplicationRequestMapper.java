package com.patrycjagalant.admissionscommittee.service.mapper;


import com.patrycjagalant.admissionscommittee.dto.ApplicationRequestDTO;
import com.patrycjagalant.admissionscommittee.entity.ApplicationRequest;

import javax.transaction.Transactional;

public class ApplicationRequestMapper {
    private ApplicationRequestMapper(){}

    @Transactional
    public static ApplicationRequestDTO mapToDto(ApplicationRequest applicationRequest) {
        ApplicationRequestDTO applicationRequestDTO = new ApplicationRequestDTO();

        applicationRequestDTO.setId(applicationRequest.getId());
        applicationRequestDTO.setRegistrationDate(applicationRequest.getRegistrationDate());
        applicationRequestDTO.setApplicant(ApplicantMapper.mapToDto(applicationRequest.getApplicant()));
        applicationRequestDTO.setFaculty(FacultyMapper.mapToDto(applicationRequest.getFaculty()));
        return applicationRequestDTO;
    }
    @Transactional
    public static ApplicationRequest mapToEntity(ApplicationRequestDTO applicationRequestDTO) {
        ApplicationRequest applicationRequest = new ApplicationRequest();
        applicationRequest.setRegistrationDate(applicationRequestDTO.getRegistrationDate());
        applicationRequest.setApplicant(ApplicantMapper.mapToEntity(applicationRequestDTO.getApplicant()));
        applicationRequest.setFaculty(FacultyMapper.mapToEntity(applicationRequestDTO.getFaculty()));
        return applicationRequest;
    }
    @Transactional
    public static void mapToEntity(ApplicationRequest applicationRequest, ApplicationRequestDTO applicationRequestDTO) {
        if (applicationRequestDTO.getApplicant() != null)
            applicationRequest.setApplicant(ApplicantMapper.mapToEntity(applicationRequestDTO.getApplicant()));
        if (applicationRequestDTO.getFaculty() != null)
            applicationRequest.setFaculty(FacultyMapper.mapToEntity(applicationRequestDTO.getFaculty()));
    }
}
