package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDTO;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import org.springframework.data.domain.Sort;
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

    // Register
    @GetMapping("/register")
    public String startRegistration(){ return "/applicants/registerApplicant"; }

    // implement PRG here - post-redirect-get
    @PostMapping("/register")
    public String submitRegistration(@Valid @ModelAttribute("applicantDTO") ApplicantDTO applicantDTO, BindingResult result){
        if (result.hasErrors()) {
            return "/applicant";
        }
        // Change new user to logged-in user data from session?
        User user = new User();
        applicantService.addApplicant(applicantDTO, user);
        String msg = "Your data has been successfully submitted.";
        return "message";
    }

    @PutMapping()
    public String editProfile(@Valid @ModelAttribute("applicantDTO") ApplicantDTO applicantDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "/editProfile";
        }
        Long id = 0L;
        applicantService.editApplicant(applicantDTO, id);
        String msg = "Changes in the faculty have been saved.";
        return "message";
    }

    //Admin only
    @GetMapping("/all")
    public String getAllApplicants(@RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer size,
                                   @RequestParam(required = false) String sortBy,
                                   Sort.Direction sort, Model model) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        int sizeNumber = size != null && size > 0 ? size : 5;
        String sortByParam = sortBy != null ? sortBy : "lastName";
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        model.addAttribute("applicants", applicantService.getAllApplicants(pageNumber, sizeNumber, sortDirection, sortByParam));
        return "applicants/applicants";
    }

//    // Block applicant
//    @PutMapping("/{id}")
//    public String changeApplicantBlockedStatus(@PathVariable Long id) {
//        boolean isBlocked = applicantService.changeBlockedStatus(id);
//        String msg = "";
//        if (isBlocked) {
//            msg = "Applicant has been blocked";
//        }
//        else {
//            msg = "Applicant has been unblocked";
//        }
//        return "message";
//    }
}
