package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.schedulercore.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/host")
public class HostController {

    @GetMapping("/monitor")
    public String monitor() {
        return "host/monitor";
    }


}
