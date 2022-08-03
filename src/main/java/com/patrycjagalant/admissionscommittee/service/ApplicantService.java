package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDTO;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    public ApplicantService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    @Transactional
    public void updateApplicantData(ApplicantDTO applicantdto, Long id) {
        Applicant current = applicantRepository.getReferenceById(id);
        ApplicantMapper.mapToEntity(applicantdto, current);
        applicantRepository.save(current);
    }

    public void addApplicant(ApplicantDTO applicantDTO) {
        Applicant newApplicant = ApplicantMapper.mapToEntity(applicantDTO);
        applicantRepository.save(newApplicant);
    }

    // View currently logged in applicant's data
    public ApplicantDTO getById(Long id) {
        Applicant applicant = applicantRepository.getReferenceById(id);
        return ApplicantMapper.mapToDto(applicant);
    }

    // Admin only
    public Page<Applicant> getAllApplicants(int page, int size, Sort.Direction sort, String sortBy) {
        return applicantRepository.findAll(PageRequest.of(page, size, Sort.by(sort, sortBy)));
//        List<ApplicantDTO> applicantDTOS = new ArrayList<>();
//        for (Applicant applicant: applicants) {
//            applicantDTOS.add(ApplicantMapper.mapToDto(applicant));
//        }
//        return applicantDTOS;
    }

    public void blockApplicant(Applicant applicant) {
        applicant.setBlocked(true);
        applicantRepository.save(applicant);
    }
    public void unblockApplicant(Applicant applicant) {
        applicant.setBlocked(false);
        applicantRepository.save(applicant);
    }
    public void deleteApplicant(Applicant applicant) { applicantRepository.delete(applicant); }
}
