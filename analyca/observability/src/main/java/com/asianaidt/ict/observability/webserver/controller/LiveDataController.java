package com.asianaidt.ict.observability.webserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LiveDataController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
