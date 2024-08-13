package com.scm.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.helper.Helper;

@Controller
@RequestMapping("/user")
public class UserController {

    // user dashbaord page
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        System.out.println("User dashboard");
        return "user/dashboard";
    }

    // user profile page

    @RequestMapping(value = "/profile")
    public String userProfile(Authentication authentication) {
        String email = Helper.getEmail(authentication);
        System.out.println("Email " + email);
        System.out.println("User profile");
        return "user/profile";
    }


}