package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final ScoreService scoreService;
    private final EnrollmentRequestService enrollmentRequestService;

    public ApplicantService(ApplicantRepository applicantRepository, UserRepository userRepository,
                            ScoreService scoreService, EnrollmentRequestService enrollmentRequestService) {
        this.userRepository = userRepository;
        this.applicantRepository = applicantRepository;
        this.scoreService = scoreService;
        this.enrollmentRequestService = enrollmentRequestService;
    }

    @Transactional
    public ApplicantDto editApplicant(ApplicantDto applicantdto, Long userID) throws NoSuchApplicantException {
        Applicant current = applicantRepository.findByUserId(userID).orElseThrow(NoSuchApplicantException::new);
        ApplicantMapper applicantMapper = new ApplicantMapper();
        applicantMapper.mapToEntity(applicantdto, current);
        return applicantMapper.mapToDto(current);
    }

    public Applicant addApplicant(ApplicantDto applicantDTO, User loggedUser) {
        ApplicantMapper applicantMapper = new ApplicantMapper();
        Applicant newApplicant = applicantMapper.mapToEntity(applicantDTO);
        newApplicant.setUser(loggedUser);
        return applicantRepository.save(newApplicant);
    }

    public ApplicantDto getByUserId(Long id) {
        Applicant applicant = applicantRepository.findByUserId(id).orElse(null);
        if (applicant != null) {
            ApplicantMapper applicantMapper = new ApplicantMapper();
            ApplicantDto applicantDto = applicantMapper.mapToDto(applicant);
            Long applicantID = applicantDto.getId();
            List<ScoreDto> scores = scoreService.getScoresForApplicant(applicantID);
            if(scores != null && !scores.isEmpty()) {
                applicantDto.setScores(scores);
            }
            List<EnrollmentRequestDto> requests = enrollmentRequestService.getAllForApplicantId(applicantID);
            if(requests != null && !requests.isEmpty()) {
                applicantDto.setApplicationRequests(requests);
            }
            return applicantDto;
        }
        else {
            return null;
        }
    }

    // Admin only
    public Page<ApplicantDto> getAllApplicants(int page, int size, Sort.Direction sort, String sortBy) {
        long facultiesTotal = applicantRepository.count();
        if((long) page * size > facultiesTotal) {
            page = 1;
            size = 5;
        }
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        Page<Applicant> applicantPage = applicantRepository.findAll(PageRequest.of(page-1, size, Sort.by(sortDirection, sortBy)));
        ApplicantMapper applicantMapper = new ApplicantMapper();
        List<ApplicantDto> applicantDtos = applicantMapper.mapToDto(applicantPage.getContent());
        return new PageImpl<>(applicantDtos, PageRequest.of(page-1, size, Sort.by(sortDirection, sortBy)), facultiesTotal);
    }

    @Transactional
    public boolean changeBlockedStatus(Long id) {
        User user = userRepository.getReferenceById(id);
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
        return user.isBlocked();
    }

    public void deleteApplicant(Long id) throws NoSuchFacultyException {
        if(applicantRepository.findById(id).isPresent()) {
            applicantRepository.deleteById(id);
        }
        else {
            throw new NoSuchFacultyException();
        }
    }
}
