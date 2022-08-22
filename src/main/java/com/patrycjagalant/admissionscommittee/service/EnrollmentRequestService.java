package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.*;
import com.patrycjagalant.admissionscommittee.dto.other.RequestWithNamesDto;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Status;
import com.patrycjagalant.admissionscommittee.entity.Subject;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchRequestException;
import com.patrycjagalant.admissionscommittee.repository.EnrollmentRequestRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.EnrollmentRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollmentRequestService {

    public static final String COULD_NOT_FIND_REQUEST = "Could not find enrollment request with ID: ";
    private final EnrollmentRequestRepository enrollmentRequestRepository;
    private final EnrollmentRequestMapper mapper;

    public void saveRequest(EnrollmentRequestDto requestDto, LocalDateTime dateTime) {
        EnrollmentRequest request = mapper.mapToEntity(requestDto);
        request.setRegistrationDate(dateTime);
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
        return requests.stream().map(mapper::mapToDto)
                .sorted(Comparator.comparing(EnrollmentRequestDto::getRegistrationDate)
                        .thenComparing(EnrollmentRequestDto::getPoints).reversed())
                .collect(Collectors.toList());
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
                .map(name ->
                        new ScoreDto(0, applicantDto.getId(), name, 0)).collect(Collectors.toList()));
        return relevantScores;
    }

    public Page<RequestWithNamesDto> getAll(int page, int size, Sort.Direction sort, String sortBy) {
        long requestsTotal = enrollmentRequestRepository.count();
        if (size > requestsTotal) {
            size = 100;
        } else if ((long) page * size > requestsTotal) {
            page = (int) requestsTotal / size + 1;
        }
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        Page<EnrollmentRequest> requestsPage = enrollmentRequestRepository
                .findAll(PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)));
        List<RequestWithNamesDto> requestsWithApplicantAndFacultyName = requestsPage
                .getContent().stream().map(mapper::mapToDtoWithNames).collect(Collectors.toList());
        return new PageImpl<>(requestsWithApplicantAndFacultyName,
                PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)), requestsTotal);
    }

    public void deleteRequest(Long id) {
        if (enrollmentRequestRepository.findById(id).isPresent()) {
            enrollmentRequestRepository.deleteById(id);
        } else {
            throw new NoSuchRequestException(COULD_NOT_FIND_REQUEST + id);
        }
    }

    public void updatePoints(EnrollmentRequestDto requestDto, Long id, Set<String> relevantSubjects) {
        ApplicantDto applicantDto = requestDto.getApplicant();
        List<ScoreDto> applicantScores = applicantDto.getScores();
        Integer averageResult = 0;
        if (applicantScores != null && !applicantScores.isEmpty()) {
        averageResult = getAverageResult(relevantSubjects, applicantScores);
        }
        requestDto.setPoints(averageResult);
        EnrollmentRequest request = enrollmentRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchRequestException(COULD_NOT_FIND_REQUEST + id));
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
        return requests.stream().map(mapper::mapToDto)
                .sorted(Comparator.comparing(EnrollmentRequestDto::getPoints).reversed())
                .collect(Collectors.toList());
    }

    @Transactional
    public void editStatus(Status status, long requestId) {
        EnrollmentRequest request = enrollmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchRequestException
                        (COULD_NOT_FIND_REQUEST + requestId));
        request.setTempStatus(status);
        enrollmentRequestRepository.save(request);
    }

    @Transactional
    public void submitStatement(long id, Status status) {
        EnrollmentRequest request = enrollmentRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchRequestException(COULD_NOT_FIND_REQUEST + id));
        request.setStatus(status);
        enrollmentRequestRepository.save(request);
    }
}