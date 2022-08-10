package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.service.mapper.EnrollmentRequestMapper;
import com.patrycjagalant.admissionscommittee.repository.EnrollmentRequestRepository;
import org.springframework.data.domain.Page;
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

    public EnrollmentRequestService(EnrollmentRequestRepository enrollmentRequestRepository, EnrollmentRequestMapper mapper) {
        this.enrollmentRequestRepository = enrollmentRequestRepository;
        this.mapper = mapper;
    }

    // Create
    public void addNewRequest(EnrollmentRequestDto enrollmentRequestDTO) {
        EnrollmentRequest enrollmentRequest = mapper.mapToEntity(enrollmentRequestDTO);
        enrollmentRequestRepository.save(enrollmentRequest);
    }

    // Read request by id
    public EnrollmentRequestDto getRequestById(Long id) {
        EnrollmentRequest request = enrollmentRequestRepository.getReferenceById(id);
        return mapper.mapToDto(request);
    }

    // Read requests for one applicant
    public List<EnrollmentRequestDto> getAllForApplicantId(Long applicantId) {
        List<EnrollmentRequest> requests = enrollmentRequestRepository.findByApplicantId(applicantId);
        return requests.stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    // Read all requests
    public Page<EnrollmentRequest> getAll(int page, int size, Sort.Direction sort, String sortBy) {
        return enrollmentRequestRepository.findAll(PageRequest.of(page, size, Sort.by(sort, sortBy)));
    }

    // Update a request
    @Transactional
    public void editApplicationRequest(EnrollmentRequestDto requestDTO, Long id) {
        EnrollmentRequest request = enrollmentRequestRepository.getReferenceById(id);
        mapper.mapToEntity(request, requestDTO);
        enrollmentRequestRepository.save(request);
    }

    // Delete a request
    public void deleteApplicationRequest(Long id) {
        enrollmentRequestRepository.deleteById(id);
    }

}
