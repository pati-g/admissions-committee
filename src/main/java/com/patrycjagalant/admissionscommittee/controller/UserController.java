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
            log.warn("Invalid UserDto: " + userDto.toString());
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.register", result);
            redirectAttributes.addFlashAttribute("user", userDto);
        }
        Long newUserId;
        try {
            newUserId = userService.registerUser(userDto);
            String msg = "User " + newUserId + " has been registered.";
            redirectAttributes.addFlashAttribute("message", msg);
            log.info("New user registered to database, ID: " + newUserId);
            return REDIRECT_REGISTER;
        } catch (UserAlreadyExistException e) {
            redirectAttributes
                    .addFlashAttribute(ERROR,
                            "An account for that username/email already exists.");
            log.warn("User already exists", e);
        }
        return REGISTER;
    }

    @RequestMapping(value = "/{username}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String editAccount(@Valid @ModelAttribute("user") UserDtoForEditing userDto,
                              BindingResult result,
                              @PathVariable("username") String username,
                              RedirectAttributes redirectAttributes) {
        if (!result.hasErrors() && userDto != null && userDto.getUsername().equalsIgnoreCase(username)) {
            try {
                userService.editUser(userDto);
            } catch (UserAlreadyExistException e) {
                redirectAttributes.addFlashAttribute
                        (ERROR, "An account for that email already exists.");
                log.warn("User already exists", e);
                return "redirect:/user/"+username+"/edit";
            }
            redirectAttributes.addAttribute(MESSAGE, "Your changes have been submitted");
        } else {
            log.warn("Invalid UserDto: " + (userDto == null ? "value is null" : userDto.toString()));
            redirectAttributes.addAttribute(ERROR, "Some fields have incorrect values, please try again.");
        }
        return REDIRECT_EDIT_ACCOUNT;
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String viewAccount(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
            model.addAttribute(USER_DTO, userDto);
            return VIEW_PROFILE;
    }

    @GetMapping("/{username}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String getAccountForm(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
        if (!model.containsAttribute(USER_DTO)) {
            model.addAttribute(USER_DTO, userDto);
        }
        return EDIT_ACCOUNT;
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
