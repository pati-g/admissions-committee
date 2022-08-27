package com.patrycjagalant.admissionscommittee.controller;

import com.patrycjagalant.admissionscommittee.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.patrycjagalant.admissionscommittee.utils.Constants.REGISTER;

/**
 * A controller class implementation from the MVC Pattern for general client-side operations,
 * such as opening home page, logging in etc.
 *
 * @author Patrycja Galant
 */

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

    /**
     * A controller method for GET requests for viewing the registration page.
     * Adds a new {@link UserDto} instance to the returned model,
     * to enable further registration of a new user into database.
     * @return the template name for registration page
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDto());
        }
        return REGISTER;
    }
}
