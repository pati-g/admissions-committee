package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import com.patrycjagalant.admissionscommittee.exceptions.UserAlreadyExistException;
import com.patrycjagalant.admissionscommittee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class UserController {

    public static final String REGISTER_USER = "registerUser";
    private final UserService userService;

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return REGISTER_USER;
        }
        UserDto existingUser = userService.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            model.addAttribute("user", new UserDto());
            model.addAttribute("registrationError", "User already exist in database");
            return REGISTER_USER;
        }
        Long newUserId;
        try {
            newUserId = userService.registerUser(userDTO);
            String msg = "User " + newUserId + " has been registered.";
            model.addAttribute("msg", msg);
            return "redirect:/";
        } catch (UserAlreadyExistException e) {
            model.addAttribute("user", new UserDto());
            model.addAttribute("message", "An account for that email already exists.");
            return REGISTER_USER;
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return REGISTER_USER;
    }

}
