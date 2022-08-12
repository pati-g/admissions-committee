package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.FileStorageException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import com.patrycjagalant.admissionscommittee.utils.ParamValidator;
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
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final ScoreService scoreService;
    private final EnrollmentRequestService enrollmentRequestService;

    @Value("${app.upload.dir:${user.home}}")
    public String filesPathString;

    public ApplicantService(ApplicantRepository applicantRepository, UserRepository userRepository,
                            ScoreService scoreService, EnrollmentRequestService enrollmentRequestService) {
        this.userRepository = userRepository;
        this.applicantRepository = applicantRepository;
        this.scoreService = scoreService;
        this.enrollmentRequestService = enrollmentRequestService;
    }

    @Transactional
    public void editApplicant(ApplicantDto applicantdto, Long userID, MultipartFile file) throws NoSuchApplicantException, FileStorageException {
        Applicant current = applicantRepository.findByUserId(userID).orElseThrow(NoSuchApplicantException::new);
        String fileName = userID + "_" + current.getLastName() + "_" + current.getFirstName() + "_certificate";
        String fileCode = saveFile(file, fileName);
        applicantdto.setCertificateUrl(fileCode);
        ApplicantMapper applicantMapper = new ApplicantMapper();
        applicantMapper.mapToEntity(applicantdto, current);
    }

    private String saveFile(MultipartFile file, String fileName) throws FileStorageException {
        Path uploadPath = Paths.get(filesPathString);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (InputStream inputStream = file.getInputStream()) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName + "." + extension);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException ioe) {
            throw new FileStorageException("Could not save file: " + fileName);
        }
    }

    public ResponseEntity<Resource> downloadFile(String id) throws NoSuchApplicantException {
        if (!ParamValidator.isNumeric(id)) {
            throw new NoSuchApplicantException();
        }
        Long idNumber = Long.parseLong(id);
        ApplicantDto dto = this.getByUserId(idNumber);
        FileSystemResource resource = new FileSystemResource(dto.getCertificateUrl());
        MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        ContentDisposition disposition = ContentDisposition.attachment().filename(Objects.requireNonNull(resource.getFilename())).build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
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
        ApplicantMapper applicantMapper = new ApplicantMapper();
        List<ApplicantDto> applicantDtos = applicantMapper.mapToDto(applicantPage.getContent());
        return new PageImpl<>(applicantDtos, PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)), facultiesTotal);
    }

    @Transactional
    public boolean changeBlockedStatus(Long id) {
        User user = userRepository.getReferenceById(id);
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
        return user.isBlocked();
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
