package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.service.SubjectService;
import com.patrycjagalant.admissionscommittee.utils.validators.ParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addSubject(@RequestParam String subjectName, Model model) {
        if (ParamValidator.isNameInvalid(subjectName)) {
            log.warn("Invalid subject name: " + subjectName
                    + ", must be between 2-150 characters long, only letters, spaces and '-' allowed");
            model.addAttribute(ERROR, "Subject name must be between 2-150 characters long, " +
                    "only letters, spaces and '-' allowed");
            return "/";
        }
        subjectService.addNewSubject(subjectName);
        model.addAttribute(MESSAGE, "Subject: " + subjectName + " has been submitted");
        return REDIRECT_FACULTIES;
    }

}
