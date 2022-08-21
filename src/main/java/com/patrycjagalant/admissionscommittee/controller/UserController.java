package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.dto.other.UserDtoForEditing;
import com.patrycjagalant.admissionscommittee.exceptions.NoSuchUserException;
import com.patrycjagalant.admissionscommittee.exceptions.UserAlreadyExistException;
import com.patrycjagalant.admissionscommittee.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.patrycjagalant.admissionscommittee.utils.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute("user", userDto);
        }
        Long newUserId;
        try {
            newUserId = userService.registerUser(userDto);
            String msg = "User " + newUserId + " has been registered.";
            redirectAttributes.addFlashAttribute("message", msg);
            return REDIRECT_REGISTER;
        } catch (UserAlreadyExistException e) {
            redirectAttributes
                    .addFlashAttribute("error", "An account for that email already exists.");
        }
        return REGISTER;
    }

    @RequestMapping(value = "/{username}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String editAccount(@Valid @ModelAttribute("user") UserDtoForEditing userDto,
                              BindingResult result,
                              @PathVariable("username") String username,
                              Model model) throws NoSuchUserException, UserAlreadyExistException {
        if (!result.hasErrors() && userDto != null && userDto.getUsername().equalsIgnoreCase(username)) {
            userService.editUser(userDto);
            model.addAttribute(MESSAGE, "Your changes have been submitted");
        }
        return "redirect:/user/{username}/edit";
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String viewAccount(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
        if (userDto != null) {
            model.addAttribute(USER_DTO, userDto);
            return VIEW_PROFILE;
        }
        return REDIRECT_HOME;
    }

    @GetMapping("/{username}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String getAccountForm(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
        if (userDto != null) {
            if (!model.containsAttribute(USER_DTO)) {
                model.addAttribute(USER_DTO, userDto);
            }
            return "editAccount";
        }
        return REDIRECT_HOME;
    }

    @RequestMapping(value = "/{username}/block", method={RequestMethod.GET, RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeApplicantBlockedStatus(@PathVariable String username, Model model) {
        String msg;
            boolean isBlocked = userService.changeBlockedStatus(username);
            if (isBlocked) {
                msg = "Applicant has been blocked";
            } else {
                msg = "Applicant has been unblocked";
            }
        model.addAttribute(MESSAGE, msg);
        return "redirect:/applicant/" + username;
    }

}
