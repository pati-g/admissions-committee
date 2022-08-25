package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.dto.SubjectDto;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.entity.Status;
import com.patrycjagalant.admissionscommittee.entity.Subject;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchSubjectException;
import com.patrycjagalant.admissionscommittee.exceptions.RequestAlreadySubmittedException;
import com.patrycjagalant.admissionscommittee.repository.EnrollmentRequestRepository;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.EnrollmentRequestMapper;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import com.patrycjagalant.admissionscommittee.utils.validators.ParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final ApplicantService applicantService;
    private final EnrollmentRequestMapper requestMapper;
    private final EnrollmentRequestService requestService;
    private final SubjectService subjectService;
    private final EnrollmentRequestRepository requestRepository;

    public Page<FacultyDto> getAllFaculties(int page, int size, Sort.Direction sort, String sortBy) {
        long facultiesTotal = facultyRepository.count();
        if (size > facultiesTotal) {
            size = 100;
        } else if ((long) page * size > facultiesTotal) {
            page = (int) facultiesTotal / size + 1;
        }
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.DESC;
        Page<Faculty> facultyPage = facultyRepository
                .findAll(PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)));
        List<FacultyDto> facultyDtos = facultyMapper.mapToDto(facultyPage.getContent());
        return new PageImpl<>(facultyDtos,
                PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)), facultiesTotal);
    }

    public FacultyDto getById(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new NoSuchFacultyException(getMessageFacultyNotFound(id)));
        FacultyDto dto = facultyMapper.mapToDto(faculty);
        if (faculty.getSubjects() != null) {
            dto.setSubjects(subjectService.getAllForFaculty(faculty.getId()));
        }
        if (faculty.getRequests() != null) {
            dto.setRequests(requestMapper.mapToDto(faculty.getRequests()));
        }
        return dto;
    }

    private String getMessageFacultyNotFound(Long id) {
        return "Faculty with ID: " + id + " could not be found.";
    }
    private String getMessageIdNotNumerical(String id) {
        return "Incorrect ID format: " + id + ", please make sure ID is numerical.";
    }

    // Accessible only to admin:
    public void deleteFaculty(Long id) {
        if (facultyRepository.findById(id).isPresent()) {
            facultyRepository.deleteById(id);
        } else {
            throw new NoSuchFacultyException(getMessageFacultyNotFound(id));
        }
    }

    @Transactional
    public FacultyDto addFaculty(FacultyDto facultyDTO) {
        Faculty faculty = facultyMapper.mapToEntity(facultyDTO);
        return facultyMapper.mapToDto(facultyRepository.save(faculty));
    }

    @Transactional
    public Faculty editFaculty(FacultyDto facultyDTO, Long id) {
        Faculty currentFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NoSuchFacultyException(getMessageFacultyNotFound(id)));
        facultyMapper.mapToEntity(currentFaculty, facultyDTO);
        return facultyRepository.save(currentFaculty);
    }

    @Transactional
    public void addNewRequest(EnrollmentRequestDto enrollmentRequestDTO)
           throws RequestAlreadySubmittedException {
        ApplicantDto applicantDto = enrollmentRequestDTO.getApplicant();
        FacultyDto facultyDto = enrollmentRequestDTO.getFaculty();
        if(applicantDto.getRequests()
                .stream()
                .map(EnrollmentRequestDto::getFaculty)
                .anyMatch(requestFaculty->requestFaculty.equals(facultyDto))) {
            log.warn("Applicant: " + applicantDto.getFullName() + " has already submitted a request for faculty: "
                    + facultyDto.getName());
            throw new RequestAlreadySubmittedException();
        }
        EnrollmentRequest request = requestMapper.mapToEntity(enrollmentRequestDTO);
        requestRepository.save(request);
        applicantService.addRequest(applicantDto, request);
        Faculty faculty = facultyRepository.findById(facultyDto.getId())
                .orElseThrow(() -> new NoSuchFacultyException("Faculty could not be found"));
        faculty.getRequests().add(request);
        request.setRegistrationDate(LocalDateTime.now());
    }

    @Transactional
    public void addSubjectToList(String facultyId, String subjectId) {
        verifyIsIdNumerical(facultyId, subjectId);
        Subject subjectToBeAdded = subjectService.getById(subjectId);
        Long id = Long.parseLong(facultyId);
        Faculty faculty = facultyRepository.findById(id).orElseThrow();
        Set<Subject> subjects = faculty.getSubjects();
        subjects.add(subjectToBeAdded);
        updateApplicantPointsForRequests(id, subjects);
    }

    private void updateApplicantPointsForRequests(Long id, Set<Subject> subjects) {
        Set<String> subjectNames = subjects
                .stream()
                .map(Subject::getName)
                .collect(Collectors.toSet());
        List<EnrollmentRequestDto> facultyRequests = requestService.getAllForFacultyId(id);
        if (facultyRequests != null && !facultyRequests.isEmpty()) {
            facultyRequests.forEach(request ->
                    requestService.updatePoints(request, request.getId(), subjectNames));
        }
    }

    @Transactional
    public void deleteSubjectFromList(String facultyId, String subjectId) {
        verifyIsIdNumerical(facultyId, subjectId);

        Subject subject = subjectService.getById(subjectId);
        Long id = Long.parseLong(facultyId);
        Faculty faculty = facultyRepository.findById(Long.parseLong(facultyId)).orElseThrow();
        Set<Subject> subjects = faculty.getSubjects();
        subjects.remove(subject);
        updateApplicantPointsForRequests(id, subjects);
    }

    private void verifyIsIdNumerical(String facultyId, String subjectId) {
        if(!ParamValidator.isIntegerOrLong(facultyId)) {
            throw new NoSuchFacultyException(getMessageIdNotNumerical(facultyId));
        } else if (!ParamValidator.isIntegerOrLong(subjectId)) {
            throw new NoSuchSubjectException(getMessageIdNotNumerical(subjectId));
        }
    }

    @Transactional
    public void calculateEligibility(Long id) {
        FacultyDto facultyDto = this.getById(id);
        int total = facultyDto.getTotalPlaces();
        int budget = facultyDto.getBudgetPlaces();
        List<EnrollmentRequestDto> allRequests = requestService.getAllForFacultyId(id);
        allRequests.forEach(request-> requestService.updatePoints(request, request.getId(),
                facultyDto.getSubjects().stream().map(SubjectDto::getName).collect(Collectors.toSet())));
        List<EnrollmentRequestDto> allSorted = allRequests
                .stream()
                .sorted(Comparator.comparing(EnrollmentRequestDto::getPoints).reversed())
                .collect(Collectors.toList());
        int nrOfRequests = allSorted.size();
        List<EnrollmentRequestDto> eligibleRequests = allSorted.subList(0, Math.min(total, nrOfRequests));
            List<EnrollmentRequestDto> eligibleForBudget = eligibleRequests.subList(0, Math.min(budget, nrOfRequests));
            if (nrOfRequests > budget) {
                List<EnrollmentRequestDto> eligibleForContract = eligibleRequests.subList(budget, eligibleRequests.size());
                eligibleForBudget.forEach(request -> requestService.editStatus(Status.BUDGET, request.getId()));
                eligibleForContract.forEach(request -> requestService.editStatus(Status.CONTRACT, request.getId()));
                allSorted.removeAll(eligibleRequests);
                if(!allSorted.isEmpty()) {
                    allSorted.forEach(request -> requestService.editStatus(Status.REJECTED, request.getId()));
                }
            } else {
            allSorted.forEach(request -> requestService.editStatus(Status.BUDGET, request.getId()));
        }
    }

}
