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

/**
 * A controller class implementation from the MVC Pattern for the
 * <br> {@link com.patrycjagalant.admissionscommittee.entity.User} model objects.
 *
 * @author Patrycja Galant
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * A controller method for POST requests for creating a new user in the database.
     * If the received {@link UserDto} instance is valid and no exceptions occurred,
     * it will be saved in the database.
     * Else, an error message will be returned to the client.
     * @param userDto a UserDto model attribute sent from the view with the user's input
     * @param result a BindingResult from the UserDto validation, includes all errors that
     *               may have occurred during validation
     * @param redirectAttributes for supplying message attributes to the view
     */
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

    /**
     * A controller method for PUT requests for editing user details.
     * If the received {@link UserDto} instance is valid and no exceptions occurred,
     * changes will be saved in the database.
     * Else, an error message will be returned to the client.
     * @param userDto a UserDto model attribute from the view with the user's input
     * @param result a BindingResult from the UserDto validation, includes all errors that
     *               may have occurred during validation
     * @param username the requested user's unique username
     * @param redirectAttributes for supplying message attributes to the view
     */
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
    /**
     * A controller method for GET requests for viewing a page with the requested user's details.
     * Adds a {@link UserDto} instance of the requested user, retrieved from the database based on
     * the username provided.
     * @param model for supplying a UserDto attribute to the view
     * @param username the requested user's unique username
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String viewAccount(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
            model.addAttribute(USER_DTO, userDto);
            return VIEW_PROFILE;
    }
    /**
     * A controller method for GET requests for viewing a form to edit
     * the requested user's details.
     * Adds a {@link UserDto} instance of the requested user, retrieved from the database based on
     * the username provided.
     * @param model for supplying a UserDto attribute to the view
     * @param username the requested user's unique username
     */
    @GetMapping("/{username}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public String getAccountForm(Model model, @PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
        if (!model.containsAttribute(USER_DTO)) {
            model.addAttribute(USER_DTO, userDto);
        }
        return EDIT_ACCOUNT;
    }

    /**
     * A controller method for PUT requests for blocking or unblocking the requested user.
     * @param username the requested user's unique username
     * @param model for supplying a message attribute to the view
     *
     */
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
