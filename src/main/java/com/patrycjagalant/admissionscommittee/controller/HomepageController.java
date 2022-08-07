package com.patrycjagalant.admissionscommittee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomepageController {
    @GetMapping(value = {"/", "/index"})
    public String homePage(Model model) {
        return "index";
    }
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        return "register";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "login";
    }

    @GetMapping("/logout-success")
    public String getLogoutPage(Model model) {
        return "logout";
    }
}
