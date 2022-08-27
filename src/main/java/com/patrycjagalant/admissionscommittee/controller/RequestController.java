package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.*;
import com.patrycjagalant.admissionscommittee.dto.other.RequestWithNamesDto;
import com.patrycjagalant.admissionscommittee.entity.Status;
import com.patrycjagalant.admissionscommittee.service.EnrollmentRequestService;
import com.patrycjagalant.admissionscommittee.utils.validators.ParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;

/**
 * A controller class implementation from the MVC Pattern for the
 * <br>{@link com.patrycjagalant.admissionscommittee.entity.EnrollmentRequest}
 * model objects.
 *
 * @author Patrycja Galant
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/request")
public class RequestController {

    private final EnrollmentRequestService requestService;

    /**
     * A controller method for DELETE requests for deleting a request from the database.
     * If the received Enrollment Request ID is correct and has been found in the database,
     * the request will be deleted.
     * Else, an error message will be returned to the client.
     * @param id a {@link String} representing the Enrollment Request's ID number in the database
     * @param redirectAttributes for supplying message attributes to the view
     */
    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteRequest(@PathVariable String id,
                                RedirectAttributes redirectAttributes) {
        if (!ParamValidator.isIntegerOrLong(id)) {
            log.warn(INCORRECT_REQUEST_ID + id);
            redirectAttributes.addAttribute(ERROR, INCORRECT_REQUEST);
        } else {
            requestService.deleteRequest(Long.parseLong(id));
            if (redirectAttributes.containsAttribute("username")) {
                return REDIRECT_EDIT_APPLICANT;
            }
        }
        return REDIRECT_ALL_REQUESTS;
    }

    /**
     * A controller method for GET requests for viewing a paginated list of Enrollment Requests.
     *
     * @param page a {@link String} representing the current page number
     * @param size a {@link String} representing the number of instances per page
     * @param sortBy a {@link String} representing the object's field, by which the list is sorted
     * @param sort a {@link Sort.Direction} for sorting order (ascending or descending)
     * @param model for supplying attributes to the view
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllRequests(@RequestParam(defaultValue = "1") String page,
                                 @RequestParam(defaultValue = "5") String size,
                                 @RequestParam(defaultValue = "registrationDate") String sortBy,
                                 Sort.Direction sort, Model model) {
        int pageNumber = ParamValidator.isIntegerOrLong(page) ? Math.max(Integer.parseInt(page), 1) : 1;
        int sizeNumber = ParamValidator.isIntegerOrLong(size) ? Math.max(Integer.parseInt(size), 0) : 5;
        Page<RequestWithNamesDto> requests = requestService.getAll(pageNumber, sizeNumber, sort, sortBy);
        model.addAttribute("sort", sort);
        model.addAttribute("sortBy", sortBy);
        return addPaginationModel(pageNumber, requests, model);
    }
    private String addPaginationModel(int page, Page<RequestWithNamesDto> paginated, Model model) {
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("requests", paginated);
        return ALL_REQUESTS;
    }

    /**
     * A controller method for GET requests for viewing a statement form for the Enrollment Request.
     *
     * If a given ID is incorrect, an error message will be returned to the client.
     * @param id a {@link String} representing the Enrollment Request's ID number in the database
     * @param model for supplying message attributes to the view
     */
    @GetMapping("/{id}/statement")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getStatementForm(@PathVariable String id,
                                   Model model) {
        if (!ParamValidator.isIntegerOrLong(id)) {
            log.warn(INCORRECT_REQUEST_ID + id);
            model.addAttribute(ERROR, INCORRECT_REQUEST);
            return ALL_REQUESTS;
        } else {
            EnrollmentRequestDto dto = requestService.getRequestById(Long.parseLong(id));
            ApplicantDto applicantDto = dto.getApplicant();
            FacultyDto facultyDto = dto.getFaculty();
            List<ScoreDto> relevantScores = requestService.getRelevantScoresForApplicant(applicantDto, facultyDto);
            model.addAttribute("request", dto);
            model.addAttribute("applicant", applicantDto);
            model.addAttribute("faculty", facultyDto);
            model.addAttribute("scores", relevantScores);
            return "requests/statement";
        }
    }

    /**
     * A controller method for GET requests for viewing a paginated list of Enrollment Requests.
     *
     * Else, an error message will be returned to the client.
     * @param id a {@link String} representing the Enrollment Request's ID number in the database
     * @param model for supplying message attributes to the view
     */
    @RequestMapping(value = "/{id}/submit-statement", method = {RequestMethod.PUT, RequestMethod.GET})
    public String submitStatement(@PathVariable String id,
                                  @RequestParam Status status,
                                  Model model) {
        if (!ParamValidator.isIntegerOrLong(id)) {
            log.warn(INCORRECT_REQUEST_ID + id);
            model.addAttribute(ERROR, INCORRECT_REQUEST);
            return "requests/allRequests";
        } else {
            requestService.submitStatement(Long.parseLong(id), status);
            return "redirect:/request/" + id + "/statement";
        }
    }
}
