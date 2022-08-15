package com.patrycjagalant.admissionscommittee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

//    @PostMapping("/{lang}")
//    public String changeLocale()
//    // Get locale from URL and switch it accordingly
//    // Get URL from HTTPRequest? - and return it with redirect
//    }


}
