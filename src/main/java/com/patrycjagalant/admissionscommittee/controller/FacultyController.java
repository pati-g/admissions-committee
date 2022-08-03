package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.service.FacultyService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;
    private static final String FACULTIES = "faculties";

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public String getAllFaculties (@RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer size,
                                   @RequestParam(required = false) String sortByParam,
                                   Sort.Direction sort, Model model) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        int sizeNumber = size != null && size > 0 ? size : 5;
        String sortBy = sortByParam != null ? sortByParam : "name";
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;

        model.addAttribute(FACULTIES, facultyService.getAllFaculties(pageNumber, sizeNumber, sortDirection, sortBy));
        return FACULTIES;
    }

    @GetMapping("/{id}")
    public String getOneFaculty(Model model, @PathVariable long id) {
        model.addAttribute("faculty", facultyService.getOne(id));
        return "faculty_view";
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
    public String addNewFaculty(@Valid @RequestBody FacultyDTO facultyDTO, BindingResult result) {

        if (result.hasErrors()) {
            return "/faculties/new";
        }

        facultyService.addFaculty(facultyDTO);
        // add a message that a new faculty has been created
        return FACULTIES;
    }

    @PutMapping("/edit/{id}")
    public String editFaculty(@Valid @RequestBody FacultyDTO facultyDTO, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return "/faculties/new";
        }

        facultyService.editFaculty(facultyDTO, id);
        // add a message that the faculty has been successfully edited
        return FACULTIES;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFaculty(@PathVariable("id") Long id) {

        facultyService.deleteFaculty(id);
        // message that faculty has been deleted
        return FACULTIES;
    }
}
