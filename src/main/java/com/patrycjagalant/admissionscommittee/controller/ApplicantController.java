package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.FileStorageException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import com.patrycjagalant.admissionscommittee.service.EnrollmentRequestService;
import com.patrycjagalant.admissionscommittee.service.ScoreService;
import com.patrycjagalant.admissionscommittee.service.mapper.UserMapper;
import com.patrycjagalant.admissionscommittee.utils.FileValidator;
import com.patrycjagalant.admissionscommittee.utils.ParamValidator;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static com.patrycjagalant.admissionscommittee.utils.Constants.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;
    private final ScoreService scoreService;
    private final FileValidator fileValidator;
    private final EnrollmentRequestService requestService;

    public ApplicantController(ApplicantService applicantService, ScoreService scoreService, FileValidator fileValidator, EnrollmentRequestService requestService) {
        this.applicantService = applicantService;
        this.scoreService = scoreService;
        this.fileValidator = fileValidator;
        this.requestService = requestService;
    }

    @GetMapping("/edit-profile")
    public String editProfileForm(Model model, @AuthenticationPrincipal User user) {
        UserMapper mapper = new UserMapper();
        UserDto userDto = mapper.mapToDTO(user);
        model.addAttribute(USER_DTO, userDto);

        ApplicantDto applicantDTO = applicantService.getByUserId(user.getId());
        if (applicantDTO != null) {
            model.addAttribute(APPLICANT_DTO, applicantDTO);
            model.addAttribute(USER_DTO, applicantDTO.getUserDetails());
            model.addAttribute("scores", applicantDTO.getScores());
            model.addAttribute("requests", applicantDTO.getRequests());
        }

        return APPLICANTS_EDIT_PROFILE;
    }

    @RequestMapping(value = "/edit-profile", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editProfile(@Valid @ModelAttribute ApplicantDto applicantDTO,
                              BindingResult result,
                              @AuthenticationPrincipal User user,
                              @RequestParam("file") MultipartFile file, BindingResult result2,
                              RedirectAttributes redirectAttributes,
                              Model model) throws NoSuchApplicantException, FileStorageException {
        if (file != null) {
            fileValidator.validate(file, result2);
        }
        if (result.hasErrors() || result2.hasErrors() || file == null) {
            model.addAttribute("error", "An error occurred during profile submission. Please try again");
            return "redirect:/applicant/edit-profile";
        }
        applicantService.editApplicant(applicantDTO, user.getId(), file);
        redirectAttributes.addFlashAttribute("message",
                "Changes in your profile have been submitted.");
        return "redirect:/applicant/edit-profile";
    }

    @GetMapping()
    public String viewProfile(Model model, @AuthenticationPrincipal User user) {
            ApplicantDto applicantDTO = applicantService.getByUserId(user.getId());
            model.addAttribute(APPLICANT_DTO, applicantDTO);
            model.addAttribute(USER_DTO, applicantDTO.getUserDetails());
            model.addAttribute("scores", applicantDTO.getScores());
            model.addAttribute("requests", applicantDTO.getRequests());
            return "applicants/viewProfile";
    }

    @RequestMapping(value = "/applicant/edit-score/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editScore(@Valid @ModelAttribute("score") ScoreDto scoreDto,
                            BindingResult result,
                            @PathVariable String id,
                            Model model) {
        if (result.hasErrors() || !ParamValidator.isNumeric(id)) {
            model.addAttribute("error", "Something went wrong, please try again");
        } else {
            scoreService.editScore(scoreDto, Long.parseLong(id));
        }
        return "redirect:/applicant";
    }

    @GetMapping("/new-score")
    public String newScoreForm(Model model) {
        ScoreDto scoreDto = new ScoreDto();
        model.addAttribute(SCORE_DTO, scoreDto);
        return "applicants/addScore";
    }

    @PostMapping("/new-score")
    public String newScoreForm(@Valid @ModelAttribute ScoreDto scoreDto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/applicant/new-score";
        }
        scoreService.addNewScore(scoreDto);
        return "redirect:/applicant";
    }

    @PostMapping("/new-request")
    public String newRequestForm(@Valid @ModelAttribute("request") EnrollmentRequestDto requestDto,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/faculties";
        }
        requestService.addNewRequest(requestDto);
        return "redirect:/applicant";
    }

    @RequestMapping(value = "/applicant/edit-request/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editRequest(@Valid @ModelAttribute EnrollmentRequestDto requestDto,
                              BindingResult result,
                              @PathVariable String id,
                              Model model) {
        if (result.hasErrors() || !ParamValidator.isNumeric(id)) {
            model.addAttribute("error", "Something went wrong, please try again");
        } else {
            requestService.editApplicationRequest(requestDto, Long.parseLong(id));
        }
        return REDIRECT_APPLICANT_ID_EDIT;
    }

    @GetMapping(value = "/download-certificate/{id}")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable("id") String id) throws NoSuchApplicantException {
        return applicantService.downloadFile(id);
    }

    //Admin only
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllApplicants(@RequestParam(required = false) String page,
                                   @RequestParam(required = false) String size,
                                   @RequestParam(defaultValue = "lastName") String sortBy,
                                   Sort.Direction sort, Model model) {
        int pageNumber = ParamValidator.isNumeric(page) ? Math.max(Integer.parseInt(page), 1) : 1;
        int sizeNumber = ParamValidator.isNumeric(size) ? Math.max(Integer.parseInt(size), 0) : 5;
        Page<ApplicantDto> applicantDTOPage = applicantService.getAllApplicants(pageNumber, sizeNumber, sort, sortBy);
        return addPaginationModel(pageNumber, applicantDTOPage, model);
    }

    private String addPaginationModel(int page, Page<ApplicantDto> paginated, Model model) {
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("applicants", paginated);
        return "applicants/applicants";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String viewApplicant(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            ApplicantDto applicantDTO = applicantService.getByUserId(id);
            model.addAttribute(APPLICANT_DTO, applicantDTO);
            model.addAttribute(USER_DTO, applicantDTO.getUserDetails());
            model.addAttribute("scores", applicantDTO.getScores());
            model.addAttribute("requests", applicantDTO.getRequests());
            return "applicants/viewProfile";
        }
        return REDIRECT_APPLICANT_ALL;
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editApplicant(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            ApplicantDto applicantDTO = applicantService.getByUserId(id);
            model.addAttribute(APPLICANT_DTO, applicantDTO);
            model.addAttribute(USER_DTO, applicantDTO.getUserDetails());
            return APPLICANTS_EDIT_PROFILE;
        }
        return REDIRECT_APPLICANT_ALL;
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editApplicantForm(@Valid @ModelAttribute ApplicantDto applicantDTO,
                                    BindingResult result,
                                    @PathVariable String id,
                                    @RequestParam("file") MultipartFile file,
                                    RedirectAttributes redirectAttributes,
                                    Model model) throws NoSuchApplicantException, FileStorageException {
        if (result.hasErrors() || !ParamValidator.isNumeric(id)) {
            return REDIRECT_APPLICANT_ID_EDIT;
        }
        applicantService.editApplicant(applicantDTO, Long.parseLong(id), file);
        return REDIRECT_APPLICANT_ID_EDIT;

    }

    // Block applicant
    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeApplicantBlockedStatus(@PathVariable String id, Model model) {
        String msg = "Incorrect ID: " + id;
        if (ParamValidator.isNumeric(id)) {
            boolean isBlocked = applicantService.changeBlockedStatus(Long.parseLong(id));
            if (isBlocked) {
                msg = "Applicant has been blocked";
            } else {
                msg = "Applicant has been unblocked";
            }
        }
        model.addAttribute("msg", msg);
        return REDIRECT_APPLICANT_ALL;
    }

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String deleteApplicant(@PathVariable String id, Model model) throws NoSuchFacultyException {
        if (id.matches("(\\d)+")) {
            long idNumber = Long.parseLong(id);
            applicantService.deleteApplicant(idNumber);
            model.addAttribute("msg", "Applicant " + idNumber + " has been deleted");
        } else {
            model.addAttribute("msg", "Incorrect ID: " + id);
        }
        return REDIRECT_APPLICANT_ALL;
    }
}
