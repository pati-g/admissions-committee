package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.dto.SubjectDto;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.service.ApplicantService;
import com.patrycjagalant.admissionscommittee.service.FacultyService;
import com.patrycjagalant.admissionscommittee.service.SubjectService;
import com.patrycjagalant.admissionscommittee.utils.validators.ParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;
    private final ApplicantService applicantService;
    private final SubjectService subjectService;

    @GetMapping
    public String getAllFaculties(@RequestParam(defaultValue = "1") String page,
                                  @RequestParam(defaultValue = "5") String size,
                                  @RequestParam(defaultValue = "id") String sortBy,
                                  Sort.Direction sort, Model model) {
        int pageNumber = ParamValidator.isNumeric(page) ? Math.max(Integer.parseInt(page), 1) : 1;
        int sizeNumber = ParamValidator.isNumeric(size) ? Math.max(Integer.parseInt(size), 0) : 5;
        Page<FacultyDto> facultyDTOPage = facultyService.getAllFaculties(pageNumber, sizeNumber, sort, sortBy);
        model.addAttribute("sort", sort);
        model.addAttribute("sortBy", sortBy);
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
    public String viewFaculty(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            try {
                FacultyDto facultyDTO = facultyService.getById(id);
                model.addAttribute(FACULTY_DTO, facultyDTO);
                model.addAttribute("subjects", facultyDTO.getSubjects());
                return "faculties/viewFaculty";
            } catch (NoSuchFacultyException e) {
                model.addAttribute("error", "Incorrect faculty ID, please try again.");
                log.warn("Exception thrown: NoSuchFacultyException, message: " + e.getMessage());
                return REDIRECT_FACULTIES;
            }
        }
        return REDIRECT_FACULTIES;
    }

    @RequestMapping(value = "/{id}/new-request", method = {RequestMethod.POST, RequestMethod.GET})
    public String newRequest(@PathVariable("id") String idString, @AuthenticationPrincipal User user, Model model) {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            EnrollmentRequestDto requestDto = new EnrollmentRequestDto();
            try {
                requestDto.setApplicant(applicantService.getByUserId(user.getId()));
                requestDto.setFaculty(facultyService.getById(id));
                requestDto.setRegistrationDate(LocalDateTime.now());
                facultyService.addNewRequest(requestDto);
                return "redirect:/applicant/" + user.getUsername();
            } catch (NoSuchApplicantException appExc) {
                log.warn("Applicant not found");
                model.addAttribute(ERROR, "Could not find the applicant, please try again");
            } catch (NoSuchFacultyException facExc) {
                log.warn("Faculty not found");
                model.addAttribute(ERROR, "Could not find requested faculty, please try again");
            }
        } else {
            model.addAttribute(ERROR, "Could not find requested faculty, please try again");
        }
        return REDIRECT_FACULTIES;
    }

    // Manage faculties:
    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getNewFacultyForm(Model model) {
        model.addAttribute(FACULTY_DTO, new FacultyDto());
        return "faculties/addFaculty";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getEditFacultyForm(Model model, @PathVariable("id") String idString) throws NoSuchFacultyException {
        if (ParamValidator.isNumeric(idString)) {
            long id = Long.parseLong(idString);
            FacultyDto facultyDTO = facultyService.getById(id);
            model.addAttribute(FACULTY_DTO, facultyDTO);
            model.addAttribute("subjects", facultyDTO.getSubjects());
            model.addAttribute("allSubjects", subjectService.getAll());
            return "faculties/editFaculty";
        }
        return REDIRECT_FACULTIES;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addFaculty(@Valid @ModelAttribute(FACULTY_DTO) FacultyDto facultyDTO,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return REDIRECT_NEW_FACULTY;
        }
        FacultyDto faculty = facultyService.addFaculty(facultyDTO);
        if (facultyDTO.equals(faculty)) {
            return REDIRECT_FACULTIES;
        } else {
            //do something
            return REDIRECT_NEW_FACULTY;
        }
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editFaculty(@Valid @ModelAttribute FacultyDto facultyDTO, BindingResult result,
                              @PathVariable Long id) throws NoSuchFacultyException {
        if (result.hasErrors()) {
            return REDIRECT_NEW_FACULTY;
        }
        facultyService.editFaculty(facultyDTO, id);
        return REDIRECT_FACULTIES;
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFaculty(@PathVariable String id) throws NoSuchFacultyException {
        //Check if faculty is connected to any request - if so, delete is not possible until requests are handled
        if (ParamValidator.isNumeric(id)) {
            long idNumber = Long.parseLong(id);
            facultyService.deleteFaculty(idNumber);

        }
        return REDIRECT_FACULTIES;
    }

    // Manage subjects
    @PostMapping("/{facultyId}/add-subject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addSubjectToList(@RequestParam String chosenSubject, @PathVariable String facultyId, Model model) {
        facultyService.addSubjectToList(facultyId, chosenSubject);

        return getRedirectUrl(facultyId);
    }

    @RequestMapping(value = "/{facultyId}/delete-subject/{subjectId}",
            method = {RequestMethod.DELETE, RequestMethod.GET})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteSubjectFromList(@PathVariable String facultyId, @PathVariable String subjectId, Model model) {
        facultyService.deleteSubjectFromList(facultyId, subjectId);
        return getRedirectUrl(facultyId);
    }

    private String getRedirectUrl(String facultyId) {
        return "redirect:/faculties/" + facultyId + "/edit";
    }

    @PostMapping("/{facultyId}/new-subject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public String createNewSubjectAndAddToList(@RequestParam String subjectName,
                                               @PathVariable String facultyId, Model model) {
        if (ParamValidator.isNameInvalid(subjectName)) {
            return "/";
        }
        SubjectDto subjectDto = subjectService.addNewSubject(subjectName);
        facultyService.addSubjectToList(facultyId, String.valueOf(subjectDto.getId()));
        return getRedirectUrl(facultyId);
    }
}
