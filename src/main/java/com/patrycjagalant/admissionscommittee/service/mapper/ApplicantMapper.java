package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApplicantMapper {

    public ApplicantDto mapToDto (Applicant applicant) {
        ScoreMapper scoreMapper = new ScoreMapper();
        EnrollmentRequestMapper enrollmentRequestMapper = new EnrollmentRequestMapper();
        log.debug("Applicant entity before using primary mapper {}", applicant);
        ApplicantDto dto = mapToDtoWithoutRelations(applicant);
        log.debug("Applicant DTO after primary mapping {}", dto);
        if (applicant.getScores() != null) {
            log.trace("Scores list to be added to Applicant DTO {}", applicant.getScores());
            dto.setScores(scoreMapper.mapToDto(applicant.getScores()));
            log.trace("Score DTOs list after mapping and adding to Applicant DTO {}", dto.getScores());
        }
        if (applicant.getRequests() != null) {
            log.trace("Requests list to be added to Applicant DTO {}", applicant.getRequests());
            dto.setRequests(enrollmentRequestMapper.mapToDto(applicant.getRequests()));
            log.trace("Request DTOs list after mapping and adding to Applicant DTO {}", dto.getRequests());
        }

        return dto;
    }

    public ApplicantDto mapToDtoWithoutRelations(Applicant applicant) {
        UserMapper userMapper = new UserMapper();
        log.debug("Applicant entity before mapping WITHOUT RELATIONAL FIELDS (scores and requests) {}", applicant);
        ApplicantDto dto = ApplicantDto.builder()
                .id(applicant.getId())
                .fullName(applicant.getFirstName() + " " + applicant.getLastName())
                .firstName(applicant.getFirstName())
                .lastName(applicant.getLastName())
                .city(applicant.getCity())
                .region(applicant.getRegion())
                .educationalInstitution(applicant.getEducationalInstitution())
                .certificateUrl(applicant.getCertificateUrl())
                .userDetails(userMapper.mapToDTO(applicant.getUser()))
                .build();
        log.debug("Applicant DTO after mapping {}", dto);
        return dto;
    }

    public List<ApplicantDto> mapToDto(List<Applicant> applicants) {
        log.debug("Mapping List<Applicant> to DTOs");
        return applicants.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Applicant mapToEntity(ApplicantDto applicantDto) {
        log.debug("Applicant DTO before mapping {}", applicantDto);
        return Applicant.builder()
                .certificateUrl(applicantDto.getCertificateUrl())
                .city(applicantDto.getCity())
                .educationalInstitution(applicantDto.getEducationalInstitution())
                .firstName(applicantDto.getFirstName())
                .lastName(applicantDto.getLastName())
                .region(applicantDto.getRegion()).build();
    }

    public void mapToEntity(ApplicantDto applicantDto, Applicant applicant) {
        log.debug("Applicant entity: {} and DTO: {} before mapping", applicant, applicantDto);
        String certificate = applicantDto.getCertificateUrl();
        String city = applicantDto.getCity();
        String institution = applicantDto.getEducationalInstitution();
        String firstname = applicantDto.getFirstName();
        String lastname = applicantDto.getLastName();
        String region = applicantDto.getRegion();

        if (certificate != null && !certificate.isBlank())
            applicant.setCertificateUrl(certificate);
        if (city != null && !city.isBlank())
            applicant.setCity(city);
        if (institution != null && !institution.isBlank())
            applicant.setEducationalInstitution(institution);
        if (firstname != null && !firstname.isBlank())
            applicant.setFirstName(firstname);
        if (lastname != null && !lastname.isBlank())
            applicant.setLastName(lastname);
        if (region != null && !region.isBlank())
            applicant.setRegion(region);
    }

    public Applicant mapToEntityWithId(ApplicantDto applicantDto) {
        log.debug("Mapping Applicant DTO with ID");
        Applicant applicant = this.mapToEntity(applicantDto);
        applicant.setId(applicantDto.getId());
        return applicant;
    }
}
