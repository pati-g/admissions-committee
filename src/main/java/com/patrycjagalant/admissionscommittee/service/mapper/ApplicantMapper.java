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

    private final UserMapper userMapper;

    public ApplicantMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ApplicantDto mapToDto(Applicant applicant) {
        log.debug("Applicant entity before mapping WITHOUT RELATIONAL FIELDS (scores and requests) {}", applicant);
        ApplicantDto dto = ApplicantDto.builder()
                .id(applicant.getId())
                .fullName(applicant.getFirstName() + " " + applicant.getLastName())
                .firstName(applicant.getFirstName())
                .lastName(applicant.getLastName())
                .city(applicant.getCity())
                .region(applicant.getRegion())
                .educationalInstitution(applicant.getEducationalInstitution())
                .userDetails(userMapper.mapToDto(applicant.getUser()))
                .build();
        log.debug("Applicant DTO after mapping {}", dto);
        String certificateUrl = applicant.getCertificateUrl();
        if (certificateUrl != null && !certificateUrl.isBlank()) {
            dto.setCertificateUrl(certificateUrl);
        }
        return dto;
    }

    public List<ApplicantDto> mapToDto(List<Applicant> applicants) {
        log.debug("Mapping List<Applicant> to DTOs");
        return applicants.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Applicant mapToEntity(ApplicantDto applicantDto) {
        log.debug("Applicant DTO before mapping {}", applicantDto);
        Applicant applicant = Applicant.builder()
                .certificateUrl(applicantDto.getCertificateUrl())
                .city(applicantDto.getCity())
                .educationalInstitution(applicantDto.getEducationalInstitution())
                .firstName(applicantDto.getFirstName())
                .lastName(applicantDto.getLastName())
                .region(applicantDto.getRegion()).build();
        log.debug("Applicant entity after mapping {}", applicant);
        return applicant;
    }

    public void mapToEntity(ApplicantDto applicantDto, Applicant applicant) {
        log.debug("Applicant entity: {} and DTO: {} before mapping", applicant, applicantDto);
        String city = applicantDto.getCity();
        String institution = applicantDto.getEducationalInstitution();
        String firstname = applicantDto.getFirstName();
        String lastname = applicantDto.getLastName();
        String region = applicantDto.getRegion();

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
