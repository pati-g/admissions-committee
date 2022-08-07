package com.patrycjagalant.admissionscommittee.service.mapper;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDTO;
import com.patrycjagalant.admissionscommittee.entity.Applicant;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicantMapper {

    public ApplicantDTO mapToDto (Applicant applicant) {
        ApplicantDTO applicantDTO = new ApplicantDTO();
        applicantDTO.setId(applicant.getId());
        applicantDTO.setCertificate(applicant.getCertificate());
        applicantDTO.setCity(applicant.getCity());
        applicantDTO.setEducationalInstitution(applicant.getEducationalInstitution());
        applicantDTO.setFirstName(applicant.getFirstName());
        applicantDTO.setLastName(applicant.getLastName());
        applicantDTO.setFullName(applicant.getLastName() + " " + applicant.getFirstName());
        applicantDTO.setRegion(applicant.getRegion());

        return applicantDTO;
    }

    public List<ApplicantDTO> mapToDto(List<Applicant> applicants) {
        ApplicantMapper mapper = new ApplicantMapper();
        return applicants.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public Applicant mapToEntity(ApplicantDTO applicantDto) {
        Applicant applicant = new Applicant();
        applicant.setCertificate(applicantDto.getCertificate());
        applicant.setCity(applicantDto.getCity());
        applicant.setEducationalInstitution(applicantDto.getEducationalInstitution());
        applicant.setFirstName(applicantDto.getFirstName());
        applicant.setLastName(applicantDto.getLastName());
        applicant.setRegion(applicantDto.getRegion());

        return applicant;
    }

    public void mapToEntity(ApplicantDTO applicantDto, Applicant applicant) {
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
