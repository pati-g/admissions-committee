package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.dto.SubjectDto;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.entity.Status;
import com.patrycjagalant.admissionscommittee.entity.Subject;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.exceptions.RequestAlreadySubmittedException;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.EnrollmentRequestMapper;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import com.patrycjagalant.admissionscommittee.utils.validators.ParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
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

    public FacultyDto findByName(String name) {
        Faculty faculty = facultyRepository.findByName(name);
        return facultyMapper.mapToDto(faculty);
    }

    public FacultyDto getById(Long id) throws NoSuchFacultyException {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(NoSuchFacultyException::new);
        FacultyDto dto = facultyMapper.mapToDto(faculty);
        if (faculty.getSubjects() != null) {
            dto.setSubjects(subjectService.getAllForFaculty(faculty.getId()));
        }
        if (faculty.getRequests() != null) {
            dto.setRequests(requestMapper.mapToDto(faculty.getRequests()));
        }
        return dto;
    }

    // Accessible only to admin:
    public void deleteFaculty(Long id) throws NoSuchFacultyException {
        if (facultyRepository.findById(id).isPresent()) {
            facultyRepository.deleteById(id);
        } else {
            throw new NoSuchFacultyException("Couldn't find faculty with id: " + id);
        }
    }

    public FacultyDto addFaculty(FacultyDto facultyDTO) {
        Faculty faculty = facultyMapper.mapToEntity(facultyDTO);
        return facultyMapper.mapToDto(facultyRepository.save(faculty));
    }

    @Transactional
    public Faculty editFaculty(FacultyDto facultyDTO, Long id) throws NoSuchFacultyException {
        Faculty currentFaculty = facultyRepository.findById(id).orElseThrow(NoSuchFacultyException::new);
        facultyMapper.mapToEntity(currentFaculty, facultyDTO);
        return currentFaculty;
    }

    @Transactional
    public void addNewRequest(EnrollmentRequestDto enrollmentRequestDTO)
            throws NoSuchApplicantException, NoSuchFacultyException, RequestAlreadySubmittedException {
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
        applicantService.addRequest(applicantDto, request);
        Faculty faculty = facultyRepository.findById(facultyDto.getId()).orElseThrow(NoSuchFacultyException::new);
        faculty.getRequests().add(request);
        requestService.saveRequest(enrollmentRequestDTO, LocalDateTime.now());
    }

    @Transactional
    public void addSubjectToList(String facultyId, String subjectId) {
        if (ParamValidator.isNumeric(facultyId) && ParamValidator.isNumeric(subjectId)) {
            Subject subjectToBeAdded = subjectService.getById(subjectId);
            Long id = Long.parseLong(facultyId);
            Faculty faculty = facultyRepository.findById(id).orElseThrow();
            Set<Subject> subjects = faculty.getSubjects();
            subjects.add(subjectToBeAdded);
            updateApplicantPointsForRequests(id, subjects);
        } else {
            throw new IllegalArgumentException();
        }
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
        if (ParamValidator.isNumeric(facultyId) && ParamValidator.isNumeric(subjectId)) {
            Subject subject = subjectService.getById(subjectId);
            Long id = Long.parseLong(facultyId);
            Faculty faculty = facultyRepository.findById(Long.parseLong(facultyId)).orElseThrow();
            Set<Subject> subjects = faculty.getSubjects();
            subjects.remove(subject);
            updateApplicantPointsForRequests(id, subjects);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void calculateEligibility(Long id) throws NoSuchFacultyException {
        FacultyDto facultyDto = this.getById(id);
        int total = facultyDto.getTotalPlaces();
        int budget = facultyDto.getBudgetPlaces();
        List<EnrollmentRequestDto> allRequests = requestService.getAllForFacultyId(id);
        allRequests.forEach(request-> requestService.updatePoints(request, request.getId(),
                facultyDto.getSubjects().stream().map(SubjectDto::getName).collect(Collectors.toSet())));
        List<EnrollmentRequestDto> allSorted = allRequests.stream().sorted(Comparator.comparing(EnrollmentRequestDto::getPoints).reversed())
                .collect(Collectors.toList());
        List<EnrollmentRequestDto> eligibleRequests = allSorted.subList(0, total);
        List<EnrollmentRequestDto> eligibleForBudget = eligibleRequests.subList(0, budget);
        List<EnrollmentRequestDto> eligibleForContract = eligibleRequests.subList(budget, eligibleRequests.size());
        eligibleForBudget.forEach(request -> requestService.editStatus(Status.BUDGET, request.getId()));
        eligibleForContract.forEach(request -> requestService.editStatus(Status.CONTRACT, request.getId()));
        allSorted.removeAll(eligibleRequests);
        allSorted.forEach(request->  requestService.editStatus(Status.REJECTED, request.getId()));
    }

}
