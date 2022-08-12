package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.repository.FacultyRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.EnrollmentRequestMapper;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;
    private final ApplicantService applicantService;
    private final EnrollmentRequestMapper requestMapper;
    private final EnrollmentRequestService requestService;

    public FacultyService(FacultyRepository facultyRepository, FacultyMapper facultyMapper, ApplicantService applicantService, EnrollmentRequestMapper requestMapper, EnrollmentRequestService requestService) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.applicantService = applicantService;
        this.requestMapper = requestMapper;
        this.requestService = requestService;
    }

    public Page<FacultyDto> getAllFaculties(int page, int size, Sort.Direction sort, String sortBy) {
        long facultiesTotal = facultyRepository.count();
        if ((long) page * size > facultiesTotal) {
            page = 1;
            size = 5;
        }

        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.DESC;
        Page<Faculty> facultyPage = facultyRepository.findAll(PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)));
        List<FacultyDto> facultyDtos = facultyMapper.mapToDto(facultyPage.getContent());
        return new PageImpl<>(facultyDtos, PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)), facultiesTotal);
    }

    public FacultyDto findByName(String name) {
        Faculty faculty = facultyRepository.findByName(name);
        return facultyMapper.mapToDto(faculty);
    }

    public FacultyDto getById(Long id) throws NoSuchFacultyException {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(NoSuchFacultyException::new);
        return facultyMapper.mapToDto(faculty);
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
    public void addNewRequest(EnrollmentRequestDto enrollmentRequestDTO) throws NoSuchApplicantException, NoSuchFacultyException {
        ApplicantDto applicantDto = enrollmentRequestDTO.getApplicant();
        EnrollmentRequest request = requestMapper.mapToEntity(enrollmentRequestDTO);
        applicantService.addRequest(applicantDto, request);
        FacultyDto facultyDto = enrollmentRequestDTO.getFaculty();
        Faculty faculty = facultyRepository.findById(facultyDto.getId()).orElseThrow(NoSuchFacultyException::new);
        faculty.getRequests().add(request);
        requestService.saveRequest(enrollmentRequestDTO);
    }
}
