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
import com.patrycjagalant.admissionscommittee.service.UserService;
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
    public static final String REDIRECT_HOME = "redirect:/";
    private final ApplicantService applicantService;
    private final ScoreService scoreService;
    private final FileValidator fileValidator;
    private final EnrollmentRequestService requestService;
    private final UserService userService;

    public ApplicantController(ApplicantService applicantService,
                               ScoreService scoreService,
                               FileValidator fileValidator,
                               EnrollmentRequestService requestService, UserService userService) {
        this.applicantService = applicantService;
        this.scoreService = scoreService;
        this.fileValidator = fileValidator;
        this.requestService = requestService;
        this.userService = userService;
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
            model.addAttribute(ERROR, "An error occurred during profile submission. Please try again");
            return "redirect:/applicant/edit-profile";
        }
        applicantService.editApplicant(applicantDTO, user.getId(), file);
        redirectAttributes.addFlashAttribute(MESSAGE,
                "Changes in your profile have been submitted.");
        return "redirect:/applicant/edit-profile";
    }

    @RequestMapping(value = "/applicant/edit-score/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editScore(@Valid @ModelAttribute("score") ScoreDto scoreDto,
                            BindingResult result,
                            @PathVariable String id,
                            Model model) {
        if (result.hasErrors() || !ParamValidator.isNumeric(id)) {
            model.addAttribute(ERROR, "Something went wrong, please try again");
        } else {
            scoreService.editScore(scoreDto, Long.parseLong(id));
        }
        return REDIRECT_APPLICANT;
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
            return "applicants/addScore";
        }
        scoreService.addNewScore(scoreDto);
        return REDIRECT_APPLICANT;
    }


    @RequestMapping(value = "/applicant/edit-request/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editRequest(@Valid @ModelAttribute EnrollmentRequestDto requestDto,
                              BindingResult result,
                              @PathVariable String id,
                              Model model) {
        if (result.hasErrors() || !ParamValidator.isNumeric(id)) {
            model.addAttribute(ERROR, "Something went wrong, please try again");
            return APPLICANTS_EDIT_PROFILE;
        } else {
            requestService.editApplicationRequest(requestDto, Long.parseLong(id));
            return REDIRECT_APPLICANT_ID_EDIT;
        }
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


    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String viewAccount(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
        if(userDto != null) {
        model.addAttribute(USER_DTO, userDto);
        ApplicantDto applicantDto = applicantService.getByUserId(userDto.getId());
        if (applicantDto != null) {
            addApplicantModel(model, applicantDto);
        }
            return VIEW_PROFILE;
        }
        return REDIRECT_HOME;
    }

    @GetMapping("/{username}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String editAccountForm(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
        if(userDto != null) {
            if(!model.containsAttribute(USER_DTO)) {
                model.addAttribute(USER_DTO, userDto);
                ApplicantDto applicantDto = applicantService.getByUserId(userDto.getId());
                if (applicantDto != null) {
                    addApplicantModel(model, applicantDto);
                }
            }
            return APPLICANTS_EDIT_PROFILE;
        }
        return REDIRECT_HOME;
    }

    @RequestMapping(value = "/{username}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String editAccount(@Valid @ModelAttribute() ApplicantDto applicantDTO,
                                    BindingResult result,
                                     @PathVariable("username") String username,
                                    RedirectAttributes redirectAttributes) throws NoSuchApplicantException, FileStorageException {
        UserDto userDto = userService.findByUsername(username);
        if (userDto == null) {
            redirectAttributes.addFlashAttribute(ERROR, "Couldn't find user with given username. Please try again.");
            return APPLICANTS_EDIT_PROFILE;
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute(USER_DTO, userDto);
            return APPLICANTS_EDIT_PROFILE;
        }
//        @RequestParam(value="file", required = false) MultipartFile file,
        applicantService.editApplicant(applicantDTO, userDto.getId());
        redirectAttributes.addFlashAttribute(MESSAGE, "Your changes have been submitted");
        return REDIRECT_APPLICANT_ID_EDIT;
    }

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
        model.addAttribute(MESSAGE, msg);
        return REDIRECT_APPLICANT_ALL;
    }

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String deleteApplicant(@PathVariable String id, Model model) throws NoSuchFacultyException {
        if (id.matches("(\\d)+")) {
            long idNumber = Long.parseLong(id);
            applicantService.deleteApplicant(idNumber);
            model.addAttribute(MESSAGE, "Applicant " + idNumber + " has been deleted");
        } else {
            model.addAttribute("error", "Incorrect ID: " + id);
        }
        return REDIRECT_APPLICANT_ALL;
    }

    private String addPaginationModel(int page, Page<ApplicantDto> paginated, Model model) {
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("applicants", paginated);
        return ALL_APPLICANTS;
    }
    private void addUserModel(Model model, Long userId) {
        model.addAttribute(USER_DTO, userService.findById(userId));
        ApplicantDto applicantDTO = applicantService.getByUserId(userId);
        if (applicantDTO != null) {
            addApplicantModel(model, applicantDTO);
        }
    }
    private void addApplicantModel(Model model, ApplicantDto applicantDTO) {
        model.addAttribute(APPLICANT_DTO, applicantDTO);
        model.addAttribute(SCORES, applicantDTO.getScores());
        model.addAttribute(REQUESTS, applicantDTO.getRequests());
    }

}
