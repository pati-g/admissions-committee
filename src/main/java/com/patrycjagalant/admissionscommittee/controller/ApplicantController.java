package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import com.patrycjagalant.admissionscommittee.service.mapper.UserMapper;
import com.patrycjagalant.admissionscommittee.utils.ParamValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/applicant")
public class ApplicantController {
    public static final String REDIRECT_APPLICANT_ALL = "redirect:/applicant/all";
    public static final String REDIRECT_APPLICANT_ID_EDIT = "redirect:/applicant/{id}/edit";
    public static final String EDIT_PROFILE = "applicants/editProfile";
    public static final String APPLICANT_DTO = "applicantDTO";
    private final ApplicantService applicantService;
    public ApplicantController(ApplicantService applicantService) { this.applicantService = applicantService; }

    @GetMapping("/edit-profile")
    public String editProfileForm(Model model, @AuthenticationPrincipal User user){
        UserMapper mapper = new UserMapper();
        UserDto userDto = mapper.mapToDTO(user);
        model.addAttribute("userDTO", userDto);

        ApplicantDto applicantDTO = applicantService.getById(user.getId());
        if (applicantDTO != null) {
            model.addAttribute(APPLICANT_DTO, applicantDTO);
        }

        return EDIT_PROFILE;
    }

    @RequestMapping(value = "/edit-profile", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editProfile(@Valid @ModelAttribute ApplicantDto applicantDTO,
                                BindingResult result,
                                @AuthenticationPrincipal User user,
                                Model model) throws NoSuchApplicantException {
        ApplicantDto applicant = applicantService.editApplicant(applicantDTO, user.getId());
        if (applicantDTO.equals(applicant)) {
            return "redirect:/view-profile";
        }
        else {
            return EDIT_PROFILE;
        }
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
            ApplicantDto applicantDTO = applicantService.getById(id);
            model.addAttribute(APPLICANT_DTO, applicantDTO);
            return "applicants/viewProfile";
        }
        return REDIRECT_APPLICANT_ALL;
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editApplicant(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            ApplicantDto applicantDTO = applicantService.getById(id);
            model.addAttribute(APPLICANT_DTO, applicantDTO);
            return EDIT_PROFILE;
        }
        return REDIRECT_APPLICANT_ALL;
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editApplicantForm(@Valid @ModelAttribute ApplicantDto applicantDTO,
                                BindingResult result,
                                @PathVariable String id,
                                Model model) throws NoSuchApplicantException {
        if (result.hasErrors() || ParamValidator.isNumeric(id)) {
            return REDIRECT_APPLICANT_ID_EDIT;
        }
        ApplicantDto applicant = applicantService.editApplicant(applicantDTO, Long.parseLong(id));
        if (applicantDTO.equals(applicant)) {
            return REDIRECT_APPLICANT_ALL;
        }
        else {
            return REDIRECT_APPLICANT_ID_EDIT;
        }
    }

    // Block applicant
    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeApplicantBlockedStatus(@PathVariable String id, Model model) {
        String msg = "Incorrect ID: " + id;
        if(ParamValidator.isNumeric(id)) {
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
        }
        else {
            model.addAttribute("msg", "Incorrect ID: " + id);
        }
        return REDIRECT_APPLICANT_ALL;
    }
}
