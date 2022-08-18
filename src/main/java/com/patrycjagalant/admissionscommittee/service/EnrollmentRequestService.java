package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.*;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Subject;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchRequestException;
import com.patrycjagalant.admissionscommittee.repository.EnrollmentRequestRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.EnrollmentRequestMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EnrollmentRequestService {

    private final EnrollmentRequestRepository enrollmentRequestRepository;
    private final EnrollmentRequestMapper mapper;

    public EnrollmentRequestService(EnrollmentRequestRepository enrollmentRequestRepository,
                                    EnrollmentRequestMapper mapper) {
        this.enrollmentRequestRepository = enrollmentRequestRepository;
        this.mapper = mapper;
    }

    public void saveRequest(EnrollmentRequestDto requestDto) {
        EnrollmentRequest request = mapper.mapToEntity(requestDto);
        Set<String> subjects = requestDto.getFaculty().getSubjects().stream()
                .map(SubjectDto::getName).collect(Collectors.toSet());
        EnrollmentRequest savedRequest = enrollmentRequestRepository.save(request);
        updatePoints(requestDto, savedRequest.getId(), subjects);
    }

    // Read request by id
    public EnrollmentRequestDto getRequestById(Long id) {
        EnrollmentRequest request = enrollmentRequestRepository.getReferenceById(id);
        Set<Subject> subjects = request.getFaculty().getSubjects();
        EnrollmentRequestDto requestDto = mapper.mapToDto(request);
        updatePoints(requestDto, id, subjects.stream().map(Subject::getName).collect(Collectors.toSet()));
        return requestDto;
    }

    // Read requests for one applicant
    public List<EnrollmentRequestDto> getAllForApplicantId(Long applicantId) {
        List<EnrollmentRequest> requests = enrollmentRequestRepository.findByApplicantId(applicantId);
        return requests.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public List<ScoreDto> getRelevantScoresForApplicant(ApplicantDto applicantDto, FacultyDto facultyDto) {
        List<String> relevantSubjectNames = facultyDto.getSubjects().stream()
                .map(SubjectDto::getName)
                .collect(Collectors.toList());

        List<ScoreDto> relevantScores = applicantDto.getScores().stream()
                .filter(score ->
                        relevantSubjectNames.stream().anyMatch(subject ->
                                subject.equalsIgnoreCase(score.getSubjectName())))
                .collect(Collectors.toList());

        relevantSubjectNames.removeAll(relevantScores.stream()
                .map(ScoreDto::getSubjectName)
                .collect(Collectors.toList()));

        relevantScores.addAll(relevantSubjectNames.stream()
                .map(name->
                        new ScoreDto(0, applicantDto.getId(), name, 0)).collect(Collectors.toList()));
        return relevantScores;
    }

    public Page<EnrollmentRequestDto> getAll(int page, int size, Sort.Direction sort, String sortBy) {
        long requestsTotal = enrollmentRequestRepository.count();
        if (size > requestsTotal) {
            size = 100;
        } else if ((long) page * size > requestsTotal) {
            page = (int) requestsTotal / size + 1;
        }
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        Page<EnrollmentRequest> requestsPage = enrollmentRequestRepository
                .findAll(PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)));
        List<EnrollmentRequestDto> requestDtos = mapper.mapToDto(requestsPage
                .getContent());

        return new PageImpl<>(requestDtos,
                PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)), requestsTotal);
    }

    @Transactional
    public void editRequest(EnrollmentRequestDto requestDTO, Long id) {
        EnrollmentRequest request = enrollmentRequestRepository.getReferenceById(id);
        mapper.mapToEntity(request, requestDTO);
        enrollmentRequestRepository.save(request);
    }

    public void deleteRequest(Long id) throws NoSuchRequestException {
        if(enrollmentRequestRepository.findById(id).isPresent()) {
            enrollmentRequestRepository.deleteById(id);
        } else {
            throw new NoSuchRequestException("Request not found, please try again.");
        }
    }
    public void updatePoints(EnrollmentRequestDto requestDto, Long id, Set<String> relevantSubjects) {
        ApplicantDto applicantDto = requestDto.getApplicant();
        List<ScoreDto> applicantScores = applicantDto.getScores();
        if(applicantScores == null || applicantScores.isEmpty()) {
            throw new RuntimeException();
        }
        Integer averageResult = getAverageResult(relevantSubjects, applicantScores);
        requestDto.setPoints(averageResult);
        EnrollmentRequest request = enrollmentRequestRepository.findById(id).orElseThrow();
        request.setPoints(averageResult);
        enrollmentRequestRepository.save(request);
    }

    private Integer getAverageResult(Set<String> relevantSubjects, List<ScoreDto> applicantScores) {
        int total = applicantScores.stream()
                .filter(score -> relevantSubjects.stream().anyMatch(subject ->
                                subject.equalsIgnoreCase(score.getSubjectName())))
                .mapToInt(ScoreDto::getResult).sum();
        int nrOfSubjects = relevantSubjects.size();
        return Math.toIntExact(Math.round((double) total / nrOfSubjects));
    }

    public List<EnrollmentRequestDto> getAllForFacultyId(Long id) {
        List<EnrollmentRequest> requests = enrollmentRequestRepository.findByFacultyId(id);
        return requests.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }
}
