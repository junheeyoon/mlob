package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLog;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleLogService;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/scheduleRunning")
public class ScheduleRunningController {
    @Autowired
    private ScheduleRunningService scheduleRunningService;

    @GetMapping("/list")
    public String logList(@PageableDefault(sort = {"id"}) Pageable pageable, Model model, Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));

        if (userRole.equals("ROLE_ADMIN")) {
            return "schedule/schedule-runningAdmin";
        } else {
            return "schedule/schedule-running";
        }
    }

    @PostMapping("/access")
    public @ResponseBody
    Map<String, Object> list(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));
        Map<String, Object> map = new HashMap<>();
        if (userRole.equals("ROLE_ADMIN")) {
            map.put("access", "admin");
        } else {
            map.put("access", "user");
        }
        return map;

    }

    @PostMapping("/admin")
    public @ResponseBody
    List<Map<String, Object>> t(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));
        List<ScheduleRunning> findAll;

        findAll = scheduleRunningService.findAll();

        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(m.getId()));
            map.put("scheduleName", m.getScheduleName());
            //map.put("host", m.getHostIp() + "(" + m.getHostName() + ")");
            map.put("dtCommand", m.getDtCommand().toString().replace("T", " "));
            map.put("scheduleDesc", m.getScheduleDesc());
            map.put("type", m.getType());
            map.put("userId", m.getUserId());
            return map;
        }).collect(Collectors.toList());

    }

    @PostMapping("/user")
    public @ResponseBody
    List<Map<String, Object>> tt(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));
        List<ScheduleRunning> findAll;

        findAll = scheduleRunningService.findByUserId(userName);

        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(m.getId()));
            map.put("scheduleName", m.getScheduleName());
            //map.put("host", m.getHostIp() + "(" + m.getHostName() + ")");
            map.put("dtCommand", m.getDtCommand().toString().replace("T", " "));
            map.put("scheduleDesc", m.getScheduleDesc());
            map.put("type", m.getType());
            map.put("userId", m.getUserId());
            System.out.println(map);
            return map;

        }).collect(Collectors.toList());

    }

}
