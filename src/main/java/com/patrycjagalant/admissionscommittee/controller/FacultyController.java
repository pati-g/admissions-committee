package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.service.FacultyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/all")
    public String getAllFaculties(Model model) {
        model.addAttribute("faculties", facultyService.getAllFaculties());
        return "faculties";
    }

// For admin only (manage faculties):
    @GetMapping("/new")
    public String addNewFacultyForm() {
        return "addFaculty";
    }
    @GetMapping("/edit")
    public String editFacultyForm(){
        return "editFaculty";
    }
    @PostMapping("/add")
    public String addNewFaculty(@Valid FacultyDTO facultyDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "faculties/new";
        }
        facultyService.addFaculty(facultyDTO);
        // add a message that a new faculty has been created
        return "faculties";
    }
    @PutMapping("/edit/{id}")
    public String editFaculty(@RequestBody FacultyDTO facultyDTO, @PathVariable Long id) {
        facultyService.editFaculty(facultyDTO, id);
        return "faculties";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteFaculty(@PathVariable("id") Long id) {
        facultyService.deleteFaculty(id);
        return "faculties";
    }
}
