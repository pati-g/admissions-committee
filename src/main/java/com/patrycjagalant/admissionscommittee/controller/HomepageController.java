package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.patrycjagalant.admissionscommittee.utils.Constants.REGISTER;

@Controller
public class HomepageController {

    @GetMapping(value = {"/", "/index"})
    public String homePage(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "login";
    }

    @GetMapping("/logout-success")
    public String showLogoutPage(Model model) {
        return "logout";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDto());
        }
        return REGISTER;
    }
}
