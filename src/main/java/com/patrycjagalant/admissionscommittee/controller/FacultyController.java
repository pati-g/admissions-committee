package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import com.patrycjagalant.admissionscommittee.service.FacultyService;
import com.patrycjagalant.admissionscommittee.utils.ParamValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static com.patrycjagalant.admissionscommittee.utils.Constants.*;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;
    private final ApplicantService applicantService;


    public FacultyController(FacultyService facultyService, ApplicantService applicantService) {
        this.facultyService = facultyService;
        this.applicantService = applicantService;
    }

    @GetMapping
    public String getAllFaculties(@RequestParam(defaultValue = "1") String page,
                                  @RequestParam(defaultValue = "5") String size,
                                  @RequestParam(defaultValue = "id") String sortBy,
                                  Sort.Direction sort, Model model) {
        int pageNumber = ParamValidator.isNumeric(page) ? Math.max(Integer.parseInt(page), 1) : 1;
        int sizeNumber = ParamValidator.isNumeric(size) ? Math.max(Integer.parseInt(size), 0) : 5;
        Page<FacultyDto> facultyDTOPage = facultyService.getAllFaculties(pageNumber, sizeNumber, sort, sortBy);
        return addPaginationModel(pageNumber, facultyDTOPage, model);
    }

    private String addPaginationModel(int page, Page<FacultyDto> paginated, Model model) {
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("faculties", paginated);
        return FACULTIES;
    }

    @GetMapping("/{id}")
    public String viewFaculty(Model model, @PathVariable("id") String idString) throws NoSuchFacultyException {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            FacultyDto facultyDTO = facultyService.getById(id);
            model.addAttribute(FACULTY_DTO, facultyDTO);
            return "faculties/facultyView";
        }
        return REDIRECT_FACULTIES;
    }

    @RequestMapping(value="/{id}/new-request", method = {RequestMethod.POST, RequestMethod.GET})
    public String newRequest(@PathVariable("id") String idString, @AuthenticationPrincipal User user, Model model) throws NoSuchFacultyException, NoSuchApplicantException {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            EnrollmentRequestDto requestDto = new EnrollmentRequestDto();
            requestDto.setApplicant(applicantService.getByUserId(user.getId()));
            requestDto.setFaculty(facultyService.getById(id));
            requestDto.setRegistrationDate(LocalDateTime.now());
            facultyService.addNewRequest(requestDto);
            return REDIRECT_APPLICANT;
        }
        return REDIRECT_FACULTIES;
    }

// For admin only (manage faculties):

    @GetMapping("/new")
    public String addNewFacultyForm(Model model) {
        model.addAttribute(FACULTY_DTO, new FacultyDto());
        return "faculties/addFaculty";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editFacultyForm(Model model, @PathVariable("id") String idString) throws NoSuchFacultyException {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            FacultyDto facultyDTO = facultyService.getById(id);
            model.addAttribute(FACULTY_DTO, facultyDTO);
            return "faculties/editFaculty";
        }
        return REDIRECT_FACULTIES;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addFaculty(@Valid @ModelAttribute(FACULTY_DTO) FacultyDto facultyDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return FACULTIES_NEW;
        }
        FacultyDto faculty = facultyService.addFaculty(facultyDTO);
        if (facultyDTO.equals(faculty)) {
            return REDIRECT_FACULTIES;
        } else {
            //do something
            return FACULTIES_NEW;
        }
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editFaculty(@Valid @ModelAttribute FacultyDto facultyDTO, BindingResult result, @PathVariable Long id) throws NoSuchFacultyException {
        if (result.hasErrors()) {
            return FACULTIES_NEW;
        }
        facultyService.editFaculty(facultyDTO, id);
        return REDIRECT_FACULTIES;
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFaculty(@PathVariable String id) throws NoSuchFacultyException {
        if (id.matches("(\\d)+")) {
            long idNumber = Long.parseLong(id);
            facultyService.deleteFaculty(idNumber);

        }
        return REDIRECT_FACULTIES;
    }
}
