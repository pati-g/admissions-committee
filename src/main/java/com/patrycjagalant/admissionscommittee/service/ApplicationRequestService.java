package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicationRequestDTO;
import com.patrycjagalant.admissionscommittee.entity.ApplicationRequest;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicationRequestMapper;
import com.patrycjagalant.admissionscommittee.repository.ApplicationRequestRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRequestService {

    private final ApplicationRequestRepository applicationRequestRepository;

    public ApplicationRequestService(ApplicationRequestRepository applicationRequestRepository) {
        this.applicationRequestRepository = applicationRequestRepository;
    }

    // Create
    public void addNewRequest(ApplicationRequestDTO applicationRequestDTO) {
        ApplicationRequest applicationRequest = ApplicationRequestMapper.mapToEntity(applicationRequestDTO);
        applicationRequestRepository.save(applicationRequest);
    }

    // Read request by id
    public ApplicationRequestDTO getRequestById(Long id) {
        ApplicationRequest request = applicationRequestRepository.getReferenceById(id);
        return ApplicationRequestMapper.mapToDto(request);
    }

    // Read requests for one applicant
    public List<ApplicationRequestDTO> getAllForApplicantId(Long id) {
        List<ApplicationRequest> requests = applicationRequestRepository.findByApplicantId(id);
        List<ApplicationRequestDTO> requestDTOS = new ArrayList<>();
        for (ApplicationRequest request: requests) {
            requestDTOS.add(ApplicationRequestMapper.mapToDto(request));
        }
        return requestDTOS;
    }

    // Read all requests
    public List<ApplicationRequestDTO> getAll() {
        List<ApplicationRequest> requests = applicationRequestRepository.findAll();
        List<ApplicationRequestDTO> requestDTOS = new ArrayList<>();
        for (ApplicationRequest request: requests) {
            requestDTOS.add(ApplicationRequestMapper.mapToDto(request));
        }
        return requestDTOS;
    }

    // Update a request
    @Transactional
    public void editApplicationRequest(ApplicationRequestDTO requestDTO, Long id) {
        ApplicationRequest request = applicationRequestRepository.getReferenceById(id);
        ApplicationRequestMapper.mapToEntity(request, requestDTO);
        applicationRequestRepository.save(request);
    }

    // Delete a request
    public void deleteApplicationRequest(Long id) {
        applicationRequestRepository.deleteById(id);
    }
}
