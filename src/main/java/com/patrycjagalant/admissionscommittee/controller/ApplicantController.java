package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDTO;
import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Applicant;
import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import com.patrycjagalant.admissionscommittee.service.mapper.ApplicantMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;
    public ApplicantController(ApplicantService applicantService) { this.applicantService = applicantService; }

    @GetMapping("/{id}/edit")
    public String editApplicantData(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            ApplicantDTO applicantDTO = applicantService.getById(id);
            model.addAttribute("applicantDTO", applicantDTO);
            return "applicants/editProfile";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editApplicant(@Valid @ModelAttribute ApplicantDTO applicantDTO,
                                BindingResult result,
                                @PathVariable Long id,
                                Model model) {
        if (result.hasErrors()) {
            return "redirect:/applicant/{id}/edit";
        }
        Applicant applicant = applicantService.editApplicant(applicantDTO, id);
        ApplicantMapper mapper = new ApplicantMapper();
        if (applicantDTO.equals(mapper.mapToDto(applicant))) {
            return "redirect:/";
        }
        else {
            return "redirect:/applicant/{id}/edit";
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
        Page<ApplicantDTO> applicantDTOPage = applicantService.getAllApplicants(pageNumber, sizeNumber, sort, sortBy);
        return addPaginationModel(pageNumber, applicantDTOPage, model);
    }

    private String addPaginationModel(int page, Page<ApplicantDTO> paginated, Model model) {
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("applicants", paginated);
        return "applicants/applicants";
    }

    // Block applicant
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeApplicantBlockedStatus(@PathVariable Long id) {
        boolean isBlocked = applicantService.changeBlockedStatus(id);
        String msg = "";
        if (isBlocked) {
            msg = "Applicant has been blocked";
        }
        else {
            msg = "Applicant has been unblocked";
        }
        return "message";
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String deleteApplicant(@PathVariable String id) {
        if (id.matches("(\\d)+")) {
            long idNumber = Long.parseLong(id);
            applicantService.deleteApplicant(idNumber);

        }
        return "redirect:/applicant/all";
    }
}
