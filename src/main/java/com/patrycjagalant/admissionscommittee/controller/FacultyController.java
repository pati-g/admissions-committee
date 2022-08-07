package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.service.FacultyService;
import com.patrycjagalant.admissionscommittee.service.mapper.FacultyMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/faculties")
public class FacultyController {
    public static final String FACULTIES_NEW = "/faculties/new";
    public static final String FACULTY_DTO = "facultyDTO";
    public static final String REDIRECT_FACULTIES = "redirect:/faculties";
    private final FacultyService facultyService;
    private static final String FACULTIES = "faculties/faculties";

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public String getAllFaculties(@RequestParam(defaultValue = "1") String page,
                                  @RequestParam(defaultValue = "5") String size,
                                  @RequestParam(defaultValue = "id") String sortBy,
                                  Sort.Direction sort, Model model) {
        int pageNumber = ParamValidator.isNumeric(page) ? Math.max(Integer.parseInt(page), 1) : 1;
        int sizeNumber = ParamValidator.isNumeric(size) ? Math.max(Integer.parseInt(size), 0) : 5;
        Page<FacultyDTO> facultyDTOPage = facultyService.getAllFaculties(pageNumber, sizeNumber, sort, sortBy);
        return addPaginationModel(pageNumber, facultyDTOPage, model);
    }

    private String addPaginationModel(int page, Page<FacultyDTO> paginated, Model model) {
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("faculties", paginated);
        return FACULTIES;
    }

    @GetMapping("/{id}")
    public String viewFaculty(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            FacultyDTO facultyDTO = facultyService.getById(id);
            model.addAttribute(FACULTY_DTO, facultyDTO);
            return "faculties/facultyView";
        }
        return REDIRECT_FACULTIES;
    }

// For admin only (manage faculties):

    @GetMapping("/new")
    public String addNewFacultyForm(Model model) {
        model.addAttribute(FACULTY_DTO, new FacultyDTO());
        return "faculties/addFaculty";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editFacultyForm(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            FacultyDTO facultyDTO = facultyService.getById(id);
            model.addAttribute(FACULTY_DTO, facultyDTO);
            return "faculties/editFaculty";
        }
        return REDIRECT_FACULTIES;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addFaculty(@Valid @ModelAttribute(FACULTY_DTO) FacultyDTO facultyDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return FACULTIES_NEW;
        }
        Faculty faculty = facultyService.addFaculty(facultyDTO);
        FacultyMapper mapper = new FacultyMapper();
        if (facultyDTO.equals(mapper.mapToDto(faculty))) {
            return FACULTIES;
        }
        else {
            //do something
            return FACULTIES_NEW;
        }
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editFaculty(@Valid @ModelAttribute FacultyDTO facultyDTO, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return FACULTIES_NEW;
        }
        facultyService.editFaculty(facultyDTO, id);
        return REDIRECT_FACULTIES;
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFaculty(@PathVariable String id) {
        if (id.matches("(\\d)+")) {
            long idNumber = Long.parseLong(id);
            facultyService.deleteFaculty(idNumber);

        }
        return REDIRECT_FACULTIES;
    }
}
