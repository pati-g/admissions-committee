package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    public ApplicantService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }
    public List<Applicant> getAllApplicants() {return applicantRepository.findAll(); }
}
