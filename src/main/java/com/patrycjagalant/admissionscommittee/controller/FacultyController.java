package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.service.FacultyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;
    public FacultyController(FacultyService facultyService) { this.facultyService = facultyService; }

    @GetMapping
    public String getAllFaculties(Model model) {
        model.addAttribute("faculties", facultyService.getAllFaculties());
        return "faculties";
    }
}
