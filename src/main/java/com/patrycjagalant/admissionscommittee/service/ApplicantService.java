package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    public ApplicantService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    @Transactional
    public void updateApplicantData(Applicant applicant, Long id) {
        //Applicant current = applicantRepository.getReferenceById(id);
        applicantRepository.save(applicant);
    }

    // Admin only
    public List<Applicant> getAllApplicants() { return applicantRepository.findAll(); }
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
