package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.FileStorageException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final ScoreService scoreService;
    private final EnrollmentRequestService enrollmentRequestService;
    private final ApplicantMapper applicantMapper;
    private final UserService userService;

    @Value("${app.upload.dir:${user.home}}")
    public String filesPathString;

    public ApplicantService(ApplicantRepository applicantRepository, ScoreService scoreService, EnrollmentRequestService enrollmentRequestService, ApplicantMapper applicantMapper, UserService userService) {
        this.applicantRepository = applicantRepository;
        this.scoreService = scoreService;
        this.enrollmentRequestService = enrollmentRequestService;
        this.applicantMapper = applicantMapper;
        this.userService = userService;
    }

    @Transactional
    public void editApplicant(ApplicantDto applicantdto, Long userID) throws NoSuchApplicantException {
        Applicant current = applicantRepository.findByUserId(userID).orElseThrow(NoSuchApplicantException::new);
        applicantMapper.mapToEntity(applicantdto, current);
    }

    @Transactional
    public void saveFile(MultipartFile file, String username) throws FileStorageException {
        if (file == null) {
            throw new FileStorageException("Error during uploading file");
        }
        Path uploadPath = Paths.get(filesPathString);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        UserDto userDto = userService.findByUsername(username);
        Applicant applicant = applicantRepository.findById(userDto.getId()).orElseThrow();
        String fileName = userDto.getId() + "_" + applicant.getLastName() + "_" + applicant.getFirstName() + "_certificate";

        try (InputStream inputStream = file.getInputStream()) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName + "." + extension);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            String pathString =  filePath.toString();
            applicant.setCertificateUrl(pathString);
        } catch (IOException ioe) {
            throw new FileStorageException("Could not save file: " + fileName);
        }
    }

    public ResponseEntity<Resource> downloadFile(String username) {
        UserDto userDto = userService.findByUsername(username);
        Optional<Applicant> applicantOptional = applicantRepository.findByUserId(userDto.getId());
        if (applicantOptional.isEmpty()) {
            log.warn("Applicant with username: " + username + " not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not find applicant with username: " + username + ", please try again");
        }
        Applicant applicant = applicantOptional.get();
        FileSystemResource resource = new FileSystemResource(applicant.getCertificateUrl());
        MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        ContentDisposition disposition = ContentDisposition.attachment().filename(Objects.requireNonNull(resource.getFilename())).build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    public Applicant addApplicant(ApplicantDto applicantDTO, User loggedUser) {
        Applicant newApplicant = applicantMapper.mapToEntity(applicantDTO);
        newApplicant.setUser(loggedUser);
        return applicantRepository.save(newApplicant);
    }

    public ApplicantDto getByUserId(Long id) {
        Applicant applicant = applicantRepository.findByUserId(id).orElse(null);
        if (applicant != null) {
            ApplicantDto applicantDto = applicantMapper.mapToDto(applicant);
            Long applicantID = applicantDto.getId();
            List<ScoreDto> scores = scoreService.getScoresForApplicant(applicantID);
            if (scores != null && !scores.isEmpty()) {
                applicantDto.setScores(scores);
            }
            List<EnrollmentRequestDto> requests = enrollmentRequestService.getAllForApplicantId(applicantID);
            if (requests != null && !requests.isEmpty()) {
                applicantDto.setRequests(requests);
            }
            return applicantDto;
        } else {
            return null;
        }
    }

    // Admin only
    public Page<ApplicantDto> getAllApplicants(int page, int size, Sort.Direction sort, String sortBy) {
        long facultiesTotal = applicantRepository.count();
        if ((long) page * size > facultiesTotal) {
            page = 1;
            size = 5;
        }
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        Page<Applicant> applicantPage = applicantRepository.findAll(PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)));
        List<ApplicantDto> applicantDtos = applicantMapper.mapToDto(applicantPage.getContent());
        return new PageImpl<>(applicantDtos, PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)), facultiesTotal);
    }



    public void deleteApplicant(Long id) throws NoSuchFacultyException {
        if (applicantRepository.findById(id).isPresent()) {
            applicantRepository.deleteById(id);
        } else {
            throw new NoSuchFacultyException();
        }
    }
@Transactional
    public void addRequest(ApplicantDto applicantDto, EnrollmentRequest request) throws NoSuchApplicantException {
        Applicant applicant = applicantRepository.findById(applicantDto.getId()).orElseThrow(NoSuchApplicantException::new);
        applicant.getRequests().add(request);
    }
}
