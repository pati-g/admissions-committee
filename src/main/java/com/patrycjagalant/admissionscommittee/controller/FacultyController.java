package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.dto.SubjectDto;
import com.patrycjagalant.admissionscommittee.entity.Faculty;
import com.patrycjagalant.admissionscommittee.entity.User;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchFacultyException;
import com.patrycjagalant.admissionscommittee.exceptions.RequestAlreadySubmittedException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;

/**
 * A controller class implementation from the MVC Pattern for the
 * <br> {@link Faculty} model objects.
 *
 * @author Patrycja Galant
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;
    private final ApplicantService applicantService;
    private final SubjectService subjectService;

    /**
     * A controller method for GET requests for viewing a paginated list of Faculties.
     *
     * @param page a {@link String} representing the current page number
     * @param size a {@link String} representing the number of instances per page
     * @param sortBy a {@link String} representing the object's field, by which the list is sorted
     * @param sort a {@link Sort.Direction} for sorting order (ascending or descending)
     * @param model for supplying attributes to the view
     */
    @GetMapping
    public String getAllFaculties(@RequestParam(defaultValue = "1") String page,
                                  @RequestParam(defaultValue = "5") String size,
                                  @RequestParam(defaultValue = "id") String sortBy,
                                  Sort.Direction sort, Model model) {
        int pageNumber = ParamValidator.isIntegerOrLong(page) ? Math.max(Integer.parseInt(page), 1) : 1;
        int sizeNumber = ParamValidator.isIntegerOrLong(size) ? Math.max(Integer.parseInt(size), 0) : 5;
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
    /**
     * A controller method for GET requests for viewing a page with the requested faculty's details.
     * Adds a {@link FacultyDto} instance of the requested faculty, as well as its subjects and Enrollment Requests,
     * retrieved from the database based on the ID provided.
     * @param model for supplying attributes to the view
     * @param idString the requested faculty's ID number
     */
    @GetMapping("/{id}")
    public String viewFaculty(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isIntegerOrLong(idString)) {
            long id = Long.parseLong(idString);
            try {
                FacultyDto facultyDto = facultyService.getById(id);
                model.addAttribute(FACULTY_DTO, facultyDto);
                model.addAttribute("subjects", facultyDto.getSubjects());
                model.addAttribute("requests", facultyDto.getRequests());
                return "faculties/viewFaculty";
            } catch (NoSuchFacultyException e) {
                model.addAttribute("error", "Incorrect faculty ID, please try again.");
                log.warn("Exception thrown: NoSuchFacultyException, message: " + e.getMessage());
                return REDIRECT_FACULTIES;
            }
        }
        log.warn("Failed to load FacultyDto, requested ID is not numerical: " + idString);
        return REDIRECT_FACULTIES;
    }

    /**
     * A controller method for POST requests for creating a new Enrollment Request for the requested faculty.
     * If the request is valid, it will be saved to database.
     * Else, an error message will be returned to the client.
     * @param idString a {@link String} representing the faculty's ID number
     * @param redirectAttributes for supplying attributes to the view
     */
    @RequestMapping(value = "/{id}/new-request", method = {RequestMethod.POST, RequestMethod.GET})
    public String newRequest(@PathVariable("id") String idString, @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes) {
        if (ParamValidator.isIntegerOrLong(idString)) {
            long facultyId = Long.parseLong(idString);
            try {
                EnrollmentRequestDto requestDto = new EnrollmentRequestDto();
                Long userId = user.getId();
                requestDto.setApplicant(applicantService.getByUserId(userId)
                        .orElseThrow(() -> new NoSuchApplicantException("Could not find applicant with user ID: "
                                + userId)));
                requestDto.setFaculty(facultyService.getById(facultyId));
                facultyService.addNewRequest(requestDto);
                return "redirect:/applicant/" + user.getUsername();
            } catch (NoSuchApplicantException appExc) {
                log.warn("Applicant not found");
                redirectAttributes.addAttribute(ERROR, "Could not find the applicant, please try again");
            } catch (NoSuchFacultyException facExc) {
                log.warn("Faculty not found");
                redirectAttributes.addAttribute(ERROR, "Could not find requested faculty, please try again");
            } catch (RequestAlreadySubmittedException e) {
                log.warn("Enrollment request already in system");
                redirectAttributes.addAttribute(ERROR, "This enrollment request already exists.");            }
        } else {
            log.warn("Faculty not found");
            redirectAttributes.addAttribute(ERROR, "Could not find requested faculty, please try again");
        }
        return REDIRECT_FACULTIES;
    }
    /**
     * A controller method for POST requests for processing an Enrollment Request.
     * If the Enrollment Request's ID is valid, it will be analyzed in the Service layer
     * to calculate the applicant's eligibility for enrollment.
     * If an error occurred, an error message will be returned to the client.
     * @param id a {@link String} representing the Enrollment Request's ID number
     * @param redirectAttributes for supplying attributes to the view
     */
    @RequestMapping(value = "/process-requests/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String processRequests(@PathVariable String id,
                                  RedirectAttributes redirectAttributes) throws NoSuchFacultyException {
        if (!ParamValidator.isIntegerOrLong(id)) {
            log.warn("Error while parsing ID, incorrect type: " + id);
            redirectAttributes.addAttribute(ERROR, "Could not find faculty with ID: " + id);
        } else {
            facultyService.calculateEligibility(Long.parseLong(id));
            redirectAttributes.addAttribute(MESSAGE, "Success!");
        }
        return REDIRECT_FACULTIES + "/" + id;
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getNewFacultyForm(Model model) {
        model.addAttribute(FACULTY_DTO, new FacultyDto());
        return "faculties/addFaculty";
    }

    /**
     * A controller method for GET requests for viewing a form to edit faculty's details.
     * If the Faculty's ID is invalid,
     * an error message will be returned to the client.
     * @param idString a {@link String} representing the Faculty's ID number
     * @param model for supplying attributes to the view
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getEditFacultyForm(Model model, @PathVariable("id") String idString) {
        if (ParamValidator.isIntegerOrLong(idString)) {
            long id = Long.parseLong(idString);
            FacultyDto facultyDTO = facultyService.getById(id);
            model.addAttribute(FACULTY_DTO, facultyDTO);
            model.addAttribute("subjects", facultyDTO.getSubjects());
            model.addAttribute("allSubjects", subjectService.getAll());
            return "faculties/editFaculty";
        }
        return REDIRECT_FACULTIES;
    }
    /**
     * A controller method for POST requests for creating a new faculty in the database.
     * If the received {@link FacultyDto} instance is valid and no exceptions occurred,
     * it will be saved in the database.
     * Else, an error message will be returned to the client.
     * @param facultyDTO a UserDto model attribute sent from the view with the user's input
     * @param result a BindingResult from the UserDto validation, includes all errors that
     *               may have occurred during validation
     * @param redirectAttributes for supplying message attributes to the view
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addFaculty(@Valid @ModelAttribute(FACULTY_DTO) FacultyDto facultyDTO,
                             BindingResult result, RedirectAttributes redirectAttributes) {
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
    /**
     * A controller method for POST requests for creating a new faculty in the database.
     * If the received {@link FacultyDto} instance is valid and no exceptions occurred,
     * it will be saved in the database.
     * Else, an error message will be returned to the client.
     * @param facultyDTO a UserDto model attribute sent from the view with the user's input
     * @param result a BindingResult from the UserDto validation, includes all errors that
     *               may have occurred during validation
     * @param id a {@link String} representation of the faculty's ID number
     */
    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editFaculty(@Valid @ModelAttribute FacultyDto facultyDTO, BindingResult result,
                              @PathVariable String id) {
        if (result.hasErrors()) {
            return REDIRECT_NEW_FACULTY;
        }
        long idNr = Long.parseLong(id);
        facultyService.editFaculty(facultyDTO, idNr);
        return REDIRECT_FACULTIES + "/" + id;
    }

    /**
     * A controller method for DELETE requests for deleting a faculty from the database.
     * If the received Faculty's ID is correct, the faculty has been found in the database
     * and it can be deleted, the request will be processed.
     * Else, an error message will be returned to the client.
     * @param id a {@link String} representing the Faculty's ID number in the database
     * @param redirectAttributes for supplying message attributes to the view
     */
    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFaculty(@PathVariable String id, RedirectAttributes redirectAttributes)  {
        //Check if faculty is connected to any request - if so, delete is not possible until requests are handled
        if (ParamValidator.isIntegerOrLong(id)) {
            long idNumber = Long.parseLong(id);
            facultyService.deleteFaculty(idNumber);

        }
        return REDIRECT_FACULTIES;
    }

    /**
     * This controller method processes the POST requests to add a subject
     * to the faculty's subjects' list.
     * @param subjectId a {@link String} representing the subject's ID number in the database
     * @param facultyId a {@link String} representing the faculty's ID number in the database
     * @param model for supplying message attributes to the view
     */
    @PostMapping("/{facultyId}/add-subject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addSubjectToList(@RequestParam("chosenSubject") String subjectId, @PathVariable String facultyId, Model model) {
        facultyService.addSubjectToList(facultyId, subjectId);

        return getRedirectUrl(facultyId);
    }

    /**
     * This controller method processes the DELETE requests to remove a subject
     * from the faculty's subjects' list.
     * @param subjectId a {@link String} representing the subject's ID number in the database
     * @param facultyId a {@link String} representing the faculty's ID number in the database
     * @param model for supplying message attributes to the view
     */
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

    /**
     * This controller method processes the POST requests to create a new subject
     * and immediately add it to the faculty's subjects' list.
     * @param subjectName a {@link String} representing the name of a newly created subject
     * @param facultyId a {@link String} representing the faculty's ID number in the database
     * @param model for supplying message attributes to the view
     */
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
