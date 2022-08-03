package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDTO;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
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
    private final UserRepository userRepository;

    public ApplicantService(ApplicantRepository applicantRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.applicantRepository = applicantRepository;
    }

    @Transactional
    public Applicant editApplicant(ApplicantDTO applicantdto, Long id) {
        Applicant current = applicantRepository.getReferenceById(id);
        ApplicantMapper.mapToEntity(applicantdto, current);
        return current;
    }

    public Applicant addApplicant(ApplicantDTO applicantDTO, User loggedUser) {
        Applicant newApplicant = ApplicantMapper.mapToEntity(applicantDTO);
        newApplicant.setUser(loggedUser);
        return applicantRepository.save(newApplicant);
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

    @Transactional
    public boolean changeBlockedStatus(Long id) {
        User user = userRepository.getReferenceById(id);
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
        return user.isBlocked();
    }

    public void deleteApplicant(Applicant applicant) { applicantRepository.delete(applicant); }
}
