package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @GetMapping("/all")
    public String getAllApplicants(Model model) {
        model.addAttribute("applicants", applicantService.getAllApplicants());
        return "applicants";
    }
    // Block applicant
    // Unblock applicant
}
