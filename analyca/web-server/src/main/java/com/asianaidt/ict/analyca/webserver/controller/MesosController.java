package com.asianaidt.ict.analyca.webserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mesos")
public class MesosController {
    @GetMapping("/monitor")
    public String monitor() {
        return "mesos/monitor";
    }
}
