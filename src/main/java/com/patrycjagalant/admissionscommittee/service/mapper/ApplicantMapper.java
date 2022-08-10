package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicantMapper {

    public ApplicantDto mapToDto (Applicant applicant) {
        ScoreMapper scoreMapper = new ScoreMapper();
        EnrollmentRequestMapper enrollmentRequestMapper = new EnrollmentRequestMapper();
        ApplicantDto dto = mapToDtoWithoutRelations(applicant);
        if (applicant.getScores() != null) {
            dto.setScores(scoreMapper.mapToDto(applicant.getScores()));
        }
        if (applicant.getEnrollmentRequests() != null) {
            dto.setApplicationRequests(enrollmentRequestMapper.mapToDto(applicant.getEnrollmentRequests()));
        }
        return dto;
    }

    public ApplicantDto mapToDtoWithoutRelations(Applicant applicant) {
        UserMapper userMapper = new UserMapper();
        return ApplicantDto.builder()
                .id(applicant.getId())
                .fullName(applicant.getLastName() + " " + applicant.getFirstName())
                .firstName(applicant.getFirstName())
                .lastName(applicant.getLastName())
                .city(applicant.getCity())
                .region(applicant.getRegion())
                .educationalInstitution(applicant.getEducationalInstitution())
                .certificate(applicant.getCertificate())
                .userDetails(userMapper.mapToDTO(applicant.getUser()))
                .build();
    }

    public List<ApplicantDto> mapToDto(List<Applicant> applicants) {
        ApplicantMapper mapper = new ApplicantMapper();
        return applicants.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public Applicant mapToEntity(ApplicantDto applicantDto) {
        return Applicant.builder()
                .certificate(applicantDto.getCertificate())
                .city(applicantDto.getCity())
                .educationalInstitution(applicantDto.getEducationalInstitution())
                .firstName(applicantDto.getFirstName())
                .lastName(applicantDto.getLastName())
                .region(applicantDto.getRegion()).build();
    }

    public void mapToEntity(ApplicantDto applicantDto, Applicant applicant) {
        String certificate = applicantDto.getCertificate();
        String city = applicantDto.getCity();
        String institution = applicantDto.getEducationalInstitution();
        String firstname = applicantDto.getFirstName();
        String lastname = applicantDto.getLastName();
        String region = applicantDto.getRegion();

        if (certificate != null && !certificate.isEmpty())
            applicant.setCertificate(certificate);
        if (city != null && !city.isEmpty())
            applicant.setCity(city);
        if (institution != null && !institution.isEmpty())
            applicant.setEducationalInstitution(institution);
        if (firstname != null && !firstname.isEmpty())
            applicant.setFirstName(firstname);
        if (lastname != null && !lastname.isEmpty())
            applicant.setLastName(lastname);
        if (region != null && !region.isEmpty())
            applicant.setRegion(region);
    }
}
