package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;
    public ApplicantController(ApplicantService applicantService) { this.applicantService = applicantService; }

    // Register
    @GetMapping()
    public String startRegistration(){
        // implement
        return "registerApplicant";
    }
    // implement PRG here - post-redirect-get, to avoid re-submitting at refresh!!
    @PostMapping
    public String submitRegistration(){
        // implement
        return "confirmRegistration";
    }

    //Admin only
    @GetMapping(path = "/all", params = {"page", "size"})
    public String getAllApplicants(@RequestParam int page, @RequestParam int size,
                                   @RequestParam String sortBy, Sort.Direction sort, Model model) {
        int pagenr = page >= 0 ? page : 0;
        model.addAttribute("applicants", applicantService.getAllApplicants(pagenr, size, sort, sortBy));
        return "applicants";
    }

    // Block applicant
    // Unblock applicant
}
