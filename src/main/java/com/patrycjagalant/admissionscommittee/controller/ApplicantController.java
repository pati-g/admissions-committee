package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/applicants")
public class ApplicantController {
    private final ApplicantService applicantService;
    public ApplicantController(ApplicantService applicantService) { this.applicantService = applicantService; }

    @GetMapping
    public String getAllApplicants(Model model) {
        model.addAttribute("applicants", applicantService.getAllApplicants());
        return "applicants";
    }
}
