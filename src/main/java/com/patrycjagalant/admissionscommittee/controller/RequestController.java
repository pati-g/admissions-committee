package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.EnrollmentRequestDto;
import com.patrycjagalant.admissionscommittee.service.EnrollmentRequestService;
import com.patrycjagalant.admissionscommittee.utils.ParamValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;

@Controller
public class RequestController {

    private final EnrollmentRequestService requestService;

    public RequestController(EnrollmentRequestService requestService) {
        this.requestService = requestService;
    }


    @RequestMapping(value = "/applicant/edit-request/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public String editRequest(@Valid @ModelAttribute EnrollmentRequestDto requestDto,
                              BindingResult result,
                              @PathVariable String id,
                              Model model) {
        if (result.hasErrors() || !ParamValidator.isNumeric(id)) {
            model.addAttribute(ERROR, "Something went wrong, please try again");
            return APPLICANTS_EDIT_PROFILE;
        } else {
            requestService.editApplicationRequest(requestDto, Long.parseLong(id));
            return REDIRECT_APPLICANT_ID_EDIT;
        }
    }

    @GetMapping("request/{id}/statement")
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
