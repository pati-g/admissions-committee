package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicationRequestDto;
import com.patrycjagalant.admissionscommittee.entity.ApplicationRequest;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicationRequestMapper;
import com.patrycjagalant.admissionscommittee.repository.ApplicationRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRequestService {

    private final ApplicationRequestRepository applicationRequestRepository;
    private final ApplicationRequestMapper mapper;

    public ApplicationRequestService(ApplicationRequestRepository applicationRequestRepository, ApplicationRequestMapper mapper) {
        this.applicationRequestRepository = applicationRequestRepository;
        this.mapper = mapper;
    }

    // Create
    public void addNewRequest(ApplicationRequestDto applicationRequestDTO) {
        ApplicationRequest applicationRequest = mapper.mapToEntity(applicationRequestDTO);
        applicationRequestRepository.save(applicationRequest);
    }

    // Read request by id
    public ApplicationRequestDto getRequestById(Long id) {
        ApplicationRequest request = applicationRequestRepository.getReferenceById(id);
        return mapper.mapToDto(request);
    }

    // Read requests for one applicant
    public List<ApplicationRequestDto> getAllForApplicantId(Long id) {
        List<ApplicationRequest> requests = applicationRequestRepository.findByApplicantId(id);
        List<ApplicationRequestDto> requestDTOS = new ArrayList<>();
        for (ApplicationRequest request: requests) {
            requestDTOS.add(mapper.mapToDto(request));
        }
        return requestDTOS;
    }

    // Read all requests
    public Page<ApplicationRequest> getAll(int page, int size, Sort.Direction sort, String sortBy) {
        return applicationRequestRepository.findAll(PageRequest.of(page, size, Sort.by(sort, sortBy)));
    }

    // Update a request
    @Transactional
    public void editApplicationRequest(ApplicationRequestDto requestDTO, Long id) {
        ApplicationRequest request = applicationRequestRepository.getReferenceById(id);
        mapper.mapToEntity(request, requestDTO);
        applicationRequestRepository.save(request);
    }

    // Delete a request
    public void deleteApplicationRequest(Long id) {
        applicationRequestRepository.deleteById(id);
    }
}
