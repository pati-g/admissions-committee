package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.ApplicantDto;
import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.dto.FacultyDto;
import com.patrycjagalant.admissionscommittee.dto.ScoreDto;
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

import java.util.List;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/request")
public class RequestController {

    public static final String INCORRECT_REQUEST = "Incorrect request ID, please try again";
    public static final String INCORRECT_REQUEST_ID = "Incorrect request ID: ";
    private final EnrollmentRequestService requestService;

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String deleteRequest(@PathVariable String id,
                                Model model) {
        if (!ParamValidator.isNumeric(id)) {
            log.warn(INCORRECT_REQUEST_ID + id);
            model.addAttribute(ERROR, INCORRECT_REQUEST);
            return APPLICANTS_EDIT_PROFILE;
        } else {
            requestService.deleteRequest(Long.parseLong(id));
            return REDIRECT_EDIT_APPLICANT;
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllRequests(@RequestParam(defaultValue = "1") String page,
                                 @RequestParam(defaultValue = "5") String size,
                                 @RequestParam(defaultValue = "registrationDate") String sortBy,
                                 Sort.Direction sort, Model model) {
        int pageNumber = ParamValidator.isNumeric(page) ? Math.max(Integer.parseInt(page), 1) : 1;
        int sizeNumber = ParamValidator.isNumeric(size) ? Math.max(Integer.parseInt(size), 0) : 5;
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
        return "requests/allRequests";
    }

    @GetMapping("/{id}/statement")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getStatementForm(@PathVariable String id,
                                   Model model) {
        if (!ParamValidator.isNumeric(id)) {
            log.warn(INCORRECT_REQUEST_ID + id);
            model.addAttribute(ERROR, INCORRECT_REQUEST);
            return APPLICANTS_EDIT_PROFILE;
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

    @RequestMapping(value = "/{id}/submit-statement", method = {RequestMethod.PUT, RequestMethod.GET})
    public String submitStatement(@PathVariable String id,
                                  @RequestParam Status status,
                                  Model model) {
        if (!ParamValidator.isNumeric(id)) {
            log.warn(INCORRECT_REQUEST_ID + id);
            model.addAttribute(ERROR, INCORRECT_REQUEST);
            return "requests/allRequests";
        } else {
            requestService.submitStatement(Long.parseLong(id), status);
            return "redirect:/request/" + id + "/statement";
        }
    }
}
