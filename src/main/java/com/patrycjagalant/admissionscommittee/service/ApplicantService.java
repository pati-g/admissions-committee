package com.patrycjagalant.admissionscommittee.service;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest;
import com.patrycjagalant.admissionscommittee.entity.Score;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.FileStorageException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchUserException;
import com.patrycjagalant.admissionscommittee.repository.ApplicantRepository;
import com.patrycjagalant.admissionscommittee.repository.UserRepository;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final ApplicantMapper applicantMapper;
    private final ScoreService scoreService;
    private final EnrollmentRequestService requestService;
    private final UserService userService;
    private final UserRepository userRepository;
    @Value("${app.upload.dir:${user.home}}")
    public String filesPathString;

    @Transactional
    public void editApplicant(ApplicantDto applicantdto, Long userID) {
        UserDto userDto = userService.findById(userID);
        applicantdto.setUserDetails(userDto);
        Applicant current = applicantRepository.findByUserId(userID).orElse(new Applicant());
        applicantMapper.mapToEntity(applicantdto, current);
        current.setUser(userRepository.findById(userID)
                .orElseThrow( () -> new NoSuchUserException("User could not be found.")));
        applicantRepository.save(current);
    }

    @Transactional
    public void saveFile(MultipartFile file, String username) {
        if (file == null) {
            log.warn("Error during uploading file - file is null");
            throw new FileStorageException("Error during uploading file");
        }
        Path uploadPath = Paths.get(filesPathString);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException exception) {
                log.warn("Unable to create filesystem directory", exception);
                throw new FileStorageException("Error during uploading file");
            }
        }
        UserDto userDto = userService.findByUsername(username);
        Applicant applicant = applicantRepository.findByUserId(userDto.getId()).orElseThrow();
        String fileName = userDto.getId() + "_" + applicant.getLastName() + "_"
                + applicant.getFirstName() + "_certificate";

        try (InputStream inputStream = file.getInputStream()) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName + "." + extension);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            String pathString = filePath.toString();
            applicant.setCertificateUrl(pathString);
        } catch (IOException ioe) {
            log.warn("Error while trying to get input stream of file: " + fileName, ioe);
            throw new FileStorageException("Could not save file: " + fileName);
        }
    }

    public ResponseEntity<Resource> downloadFile(String username) {
        UserDto userDto = userService.findByUsername(username);
        Optional<Applicant> applicantOptional = applicantRepository.findByUserId(userDto.getId());
        if (applicantOptional.isEmpty()) {
            log.warn("Applicant with username: " + username + " not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find applicant with username: "
                    + username + ", please try again");
        }
        Applicant applicant = applicantOptional.get();
        FileSystemResource resource = new FileSystemResource(applicant.getCertificateUrl());
        MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(Objects.requireNonNull(resource.getFilename())).build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    public Applicant addApplicant(ApplicantDto applicantDTO, User loggedUser) {
        Applicant newApplicant = applicantMapper.mapToEntity(applicantDTO);
        newApplicant.setUser(loggedUser);
        return applicantRepository.save(newApplicant);
    }

    public Optional<ApplicantDto> getByUserId(Long id) {
        Optional<Applicant> applicant = applicantRepository.findByUserId(id);
        if (applicant.isEmpty()) {
            log.warn("Applicant with user ID: " + id + "not found.");
        }
        return Optional.ofNullable(getDto(applicant.orElse(null)));
    }

    private ApplicantDto getDto(Applicant applicant) {
        ApplicantDto applicantDto = null;
        if (applicant != null) {
            applicantDto = applicantMapper.mapToDto(applicant);
            Long applicantID = applicantDto.getId();
            if (applicant.getScores() != null) {
                applicantDto.setScores(scoreService.getAllForApplicantId(applicantID));
            }
            if (applicant.getRequests() != null) {
                applicantDto.setRequests(requestService.getAllForApplicantId(applicantID));
            }
        }
        return applicantDto;
    }

    public Page<ApplicantDto> getAllApplicants(int page, int size, Sort.Direction sort, String sortBy) {
        long applicantsTotal = applicantRepository.count();
        if (size > applicantsTotal) {
            size = 100;
        } else if ((long) page * size > applicantsTotal) {
            page = (int) applicantsTotal / size + 1;
        }
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        Page<Applicant> applicantPage = applicantRepository
                .findAll(PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)));
        List<ApplicantDto> applicantDtos = applicantPage
                .getContent()
                .stream()
                .map(this::getDto)
                .collect(Collectors.toList());

        return new PageImpl<>(applicantDtos,
                PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy)), applicantsTotal);
    }

    public void safeDeleteApplicant(Long id) {
        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new NoSuchApplicantException("Applicant with id: " + id + " not found."));
        applicant.getUser().setEnabled(false);
    }

//    public void deleteApplicantPermanently(Long id) {
//        Applicant applicant = applicantRepository.findById(id)
//                .orElseThrow(() -> new NoSuchApplicantException("Applicant with id: " + id + " not found."));
//        User user = applicant.getUser();
//        List<Score> scores = applicant.getScores();
//        List<EnrollmentRequest> requests = applicant.getRequests();
//        // Delete all above before deleting applicant
//        applicantRepository.deleteById(id);
//    }

    @Transactional
    public void addRequest(ApplicantDto applicantDto, EnrollmentRequest request) {
        Applicant applicant = applicantRepository
                .findById(applicantDto.getId())
                .orElseThrow(() -> new NoSuchApplicantException
                        ("Applicant could not be found, please check if ID is correct."));
        applicant.getRequests().add(request);
    }
}
