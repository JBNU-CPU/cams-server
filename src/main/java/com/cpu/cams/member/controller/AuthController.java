package com.cpu.cams.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "logout";
    }

}
