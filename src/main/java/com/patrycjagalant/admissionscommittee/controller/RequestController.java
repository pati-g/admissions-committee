package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchRequestException;
import com.patrycjagalant.admissionscommittee.service.EnrollmentRequestService;
import com.patrycjagalant.admissionscommittee.utils.ParamValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;

@Controller
@RequestMapping("/request")
public class RequestController {

    private final EnrollmentRequestService requestService;

    public RequestController(EnrollmentRequestService requestService) {
        this.requestService = requestService;
    }


    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String deleteRequest(@PathVariable String id,
                              Model model) throws NoSuchRequestException {
        if (!ParamValidator.isNumeric(id)) {
            model.addAttribute(ERROR, "Incorrect request ID, please try again");
            return APPLICANTS_EDIT_PROFILE;
        } else {
            requestService.deleteRequest(Long.parseLong(id));
            return REDIRECT_APPLICANT_ID_EDIT;
        }
    }

    @GetMapping("/{id}/statement")
    public String getStatementForm(@PathVariable String id,
                                   Model model) {
        if (!ParamValidator.isNumeric(id)) {
            model.addAttribute(ERROR, "Something went wrong, please try again");
            return APPLICANTS_EDIT_PROFILE;
        }
        else {
            EnrollmentRequestDto dto = requestService.getRequestById(Long.parseLong(id));
            model.addAttribute("request", dto);
            model.addAttribute("applicant", dto.getApplicant());
            model.addAttribute("faculty", dto.getFaculty());
            model.addAttribute("scores", dto.getApplicant().getScores());
            return "requests/statement";
        }
    }
}
