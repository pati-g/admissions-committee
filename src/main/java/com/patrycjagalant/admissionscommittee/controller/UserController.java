package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchApplicantException;
import com.patrycjagalant.admissionscommittee.exceptions.UserAlreadyExistException;
import com.patrycjagalant.admissionscommittee.service.UserService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;
import static com.patrycjagalant.admissionscommittee.utils.Constants.REDIRECT_APPLICANT_ID_EDIT;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if(!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDto());
        }
        return REGISTER;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute("user", userDto);
        }
        Long newUserId;
        try {
            newUserId = userService.registerUser(userDto);
            String msg = "User " + newUserId + " has been registered.";
            redirectAttributes.addFlashAttribute("message", msg);
            return REDIRECT_REGISTER;
        } catch (UserAlreadyExistException e) {
            redirectAttributes.addFlashAttribute("error", "An account for that email already exists.");
        }
        return REGISTER;
    }

    @RequestMapping(value = "/user/{username}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String editUser(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result,
                           @PathVariable("username") String username,
                           Model model) throws NoSuchApplicantException {
        if (!result.hasErrors() && userDto != null && userDto.getUsername().equalsIgnoreCase(username)) {
            userService.editUser(userDto);
            model.addAttribute(MESSAGE, "Your changes have been submitted");
        }
        return REDIRECT_APPLICANT_ID_EDIT;
    }

}
