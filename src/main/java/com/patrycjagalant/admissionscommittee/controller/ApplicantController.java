package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.exceptions.FileStorageException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.service.*;
import com.patrycjagalant.admissionscommittee.utils.FileValidator;
import com.patrycjagalant.admissionscommittee.utils.ParamValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static com.patrycjagalant.admissionscommittee.utils.Constants.*;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/applicant")
public class ApplicantController {
    public static final String REDIRECT_HOME = "redirect:/";
    private final ApplicantService applicantService;
    private final ScoreService scoreService;
    private final UserService userService;
    private final FileValidator fileValidator;
    private final SubjectService subjectService;

    public ApplicantController(ApplicantService applicantService,
                               ScoreService scoreService,
                               FileValidator fileValidator,
                               UserService userService, SubjectService subjectService) {
        this.applicantService = applicantService;
        this.scoreService = scoreService;
        this.fileValidator = fileValidator;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String viewAccountAndProfile(Model model, @PathVariable("username") String username) {
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
    public String getProfileForm(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
        if(userDto != null) {
            if(!model.containsAttribute("username")) {
                model.addAttribute("username", userDto.getUsername());
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
    public String editProfile(@Valid @ModelAttribute ApplicantDto applicantDTO,
                              BindingResult result,
                              @PathVariable String username,
                              RedirectAttributes redirectAttributes) throws NoSuchApplicantException {
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
        applicantService.editApplicant(applicantDTO, userDto.getId());
        redirectAttributes.addFlashAttribute(MESSAGE, "Your changes have been submitted");
        return REDIRECT_APPLICANT_ID_EDIT;
    }

    @PostMapping("/save-certificate/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String saveCertificate(@ModelAttribute("file") MultipartFile file,
                                  @PathVariable String username, Model model) throws FileStorageException {
        boolean isValid = fileValidator.validate(file);
        if (!isValid) {
            return APPLICANTS_EDIT_PROFILE;
        }
        applicantService.saveFile(file, username);
        return REDIRECT_HOME;
    }

    @GetMapping(value = "/download-certificate/{username}")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable String username) {
            return applicantService.downloadFile(username);
    }

    @PostMapping("/new-score")
    public String addNewScore(@Valid @ModelAttribute ScoreDto scoreDto,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "applicants/addScore";
        }
        scoreService.addNewScore(scoreDto);
        return REDIRECT_APPLICANT;
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

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    private void addApplicantModel(Model model, ApplicantDto applicantDTO) {
        model.addAttribute(APPLICANT_DTO, applicantDTO);
        model.addAttribute(SCORES, applicantDTO.getScores());
        model.addAttribute(REQUESTS, applicantDTO.getRequests());
        model.addAttribute("newScore", new ScoreDto(applicantDTO));
        model.addAttribute("allSubjects", subjectService.getAll());
    }

}
