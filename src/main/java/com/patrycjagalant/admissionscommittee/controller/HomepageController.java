package com.patrycjagalant.admissionscommittee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {

    @RequestMapping("/")
    public String homePage() {
        return "index";
    }
}
