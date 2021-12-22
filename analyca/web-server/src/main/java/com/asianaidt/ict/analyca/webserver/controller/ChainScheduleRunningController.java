package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.containerdomain.service.ContainerLogService;
import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleLog;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleRunning;
import com.asianaidt.ict.analyca.domain.schedulercore.service.*;
import com.asianaidt.ict.analyca.webserver.service.ScheduleRunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ChainScheduleRunning")
public class ChainScheduleRunningController {
    private final SimpMessagingTemplate messagingTemplate;
    private final AgentService agentService;
    private final ScheduleRunService scheduleRunService;
    private final ScheduleService scheduleService;
    private final ScheduleStepService scheduleStepService;
    private final ScheduleRunningService scheduleRunningService;
    private final ScheduleLogWorkflowService scheduleLogWorkflowService;
    private final ContainerLogService containerLogService;

    private MessageHeaders createHeaders(@Nullable String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if (sessionId != null) headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @Autowired
    private ScheduleLogService scheduleLogService;


    @PostMapping("/access")
    public @ResponseBody
    Map<String, Object> access(Authentication authentication) {
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
    List<Map<String, Object>> admin() {
        List<ScheduleRunning> findAll = scheduleRunningService.findAll();
        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("idSchedule", m.getIdWorkflow());
            map.put("scheduleDesc", m.getScheduleDesc());
            map.put("scheduleName", m.getScheduleName());
            if (m.getDtCommand().toString().replace("T", " ").length() == 16) {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ") + ":00");
            } else {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ").substring(0,19));
            }
            map.put("type", m.getType());
            map.put("sequential", m.getSequential());
            map.put("stepName", m.getStepName());
            //System.out.println(m.getStatus());
            map.put("status", m.getStatus());
            return map;
        }).collect(Collectors.toList());

    }

    @PostMapping("/user")
    public @ResponseBody
    List<Map<String, Object>> user(Authentication authentication) {

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));
        List<ScheduleRunning> findAll = scheduleRunningService.findByUserId(userName);
        System.out.println(userName);
        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("idSchedule", m.getIdWorkflow());
            map.put("scheduleDesc", m.getScheduleDesc());
            map.put("scheduleName", m.getScheduleName());
            if (m.getDtCommand().toString().replace("T", " ").length() == 16) {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ") + ":00");
            } else {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ").substring(0,19));
            }
            map.put("type", m.getType());
            map.put("sequential", m.getSequential());
            map.put("stepName", m.getStepName());
            //System.out.println(m.getStatus());
            map.put("status", m.getStatus());
            return map;
        }).collect(Collectors.toList());

    }

    @GetMapping("/running")
    public String running(@PageableDefault(sort = {"id"}) Pageable pageable, Authentication authentication, Principal principal) {

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));

        if (userRole.equals("ROLE_ADMIN")) {
            return "schedule/schedule-running2Admin";
        } else {
            return "schedule/schedule-running2";
        }
    }

    @PostMapping("/runningList")
    public @ResponseBody
    List<Map<String, Object>> runningList(@PageableDefault(sort = {"id"}) Pageable pageable, Principal principal) {

        List<ScheduleRunning> findAll = scheduleRunningService.findAll();
        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("idSchedule", m.getIdWorkflow());
            map.put("scheduleDesc", m.getScheduleDesc());
            map.put("scheduleName", m.getScheduleName());
            if (m.getDtCommand().toString().replace("T", " ").length() == 16) {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ") + ":00");
            } else {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ").substring(0,19));
            }
            map.put("type", m.getType());
            map.put("sequential", m.getSequential());
            map.put("stepName", m.getStepName());
            map.put("status", m.getStatus());
            return map;
        }).collect(Collectors.toList());
    }
}
