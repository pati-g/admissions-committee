package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.FacultyDTO;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.service.FacultyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;
    private static final String FACULTIES = "faculties/faculties";
    private static final String RESULT_MESSAGE = "message";

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public String getAllFaculties(@RequestParam(defaultValue = "1") String page,
                                  @RequestParam(defaultValue = "5") String size,
                                  @RequestParam(defaultValue = "id") String sortBy,
                                  Sort.Direction sort, Model model) {
        int pageNumber = isNumeric(page) ? Math.max(Integer.parseInt(page), 1) : 1;
        int sizeNumber = isNumeric(size) ? Math.max(Integer.parseInt(size), 0) : 5;
        Page<FacultyDTO> facultyDTOPage = facultyService.getAllFaculties(pageNumber, sizeNumber, sort, sortBy);
        return addPaginationModel(pageNumber, facultyDTOPage, model);
    }

    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
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
        if (isNumeric(idString)) {
            long id = Long.parseLong(idString);
            FacultyDTO facultyDTO = facultyService.getOne(id);
            model.addAttribute("facultyDTO", facultyDTO);
            return "faculties/facultyView";
        }
        return "redirect:/faculties";
    }

// For admin only (manage faculties):

    @GetMapping("/new")
    public String addNewFacultyForm(Model model) {
        model.addAttribute("facultyDTO", new FacultyDTO());
        return "faculties/addFaculty";
    }

    @GetMapping("/{id}/edit")
    public String editFacultyForm(Model model, @PathVariable("id") String idString) {
        if (isNumeric(idString)) {
            long id = Long.parseLong(idString);
            FacultyDTO facultyDTO = facultyService.getOne(id);
            model.addAttribute("facultyDTO", facultyDTO);
            return "faculties/editFaculty";
        }
        return "redirect:/faculties";
    }

    @PostMapping("/add")
    public String addFaculty(@Valid @ModelAttribute("facultyDTO") FacultyDTO facultyDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/faculties/new";
        }
        Faculty faculty = facultyService.addFaculty(facultyDTO);
        String msg = "Faculty has been successfully created.";
        return RESULT_MESSAGE;
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editFaculty(@Valid @ModelAttribute FacultyDTO facultyDTO, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return "/faculties/new";
        }
        facultyService.editFaculty(facultyDTO, id);
        String msg = "Changes in the faculty have been saved.";
        return "redirect:/faculties";
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String deleteFaculty(@PathVariable String id) {
        if (id.matches("(\\d)+")) {
            long idNumber = Long.parseLong(id);
            facultyService.deleteFaculty(idNumber);

        }
        String msg = "Faculty has been deleted.";
        return "redirect:/faculties";
    }
}
