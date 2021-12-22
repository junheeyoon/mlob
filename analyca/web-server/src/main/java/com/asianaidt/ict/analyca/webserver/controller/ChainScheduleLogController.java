package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerLog;
import com.asianaidt.ict.analyca.domain.containerdomain.model.ContainerStatus;
import com.asianaidt.ict.analyca.domain.containerdomain.service.ContainerLogService;
import com.asianaidt.ict.analyca.domain.containerdomain.service.ContainerStatusService;
import com.asianaidt.ict.analyca.domain.schedulercore.model.*;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ChainScheduleLog")
public class ChainScheduleLogController {
    private final SimpMessagingTemplate messagingTemplate;
    private final AgentService agentService;
    private final ScheduleRunService scheduleRunService;
    private final ScheduleService scheduleService;
    private final ScheduleStepService scheduleStepService;
    private final ScheduleRunningService scheduleRunningService;
    private final ScheduleLogWorkflowService scheduleLogWorkflowService;
    private final ScheduleLogStepService scheduleLogStepService;
    private final ContainerLogService containerLogService;
    private final ContainerStatusService containerStatusService;
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

        List<ScheduleLogStep> findAll = scheduleLogStepService.findAllByOrderByIdDesc();
        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(m.getId()));
            map.put("scheduleName", m.getScheduleName());
            map.put("stepName", m.getStepName());
            if (m.getDtCommand().toString().replace("T", " ").length() == 16) {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ") + ":00");
            } else {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ").substring(0,19));
            }

            if(m.getDtStart() == null){
                map.put("dtStart", "");
            }
            else{
                if (m.getDtStart().toString().replace("T", " ").length() == 16) {
                    map.put("dtStart", m.getDtStart().toString().replace("T", " ") + ":00");
                } else {
                    map.put("dtStart", m.getDtStart().toString().replace("T", " ").substring(0,19));
                }
            }

            if(m.getDtEnd() == null){
                map.put("dtEnd", "");
            }
            else{
                if (m.getDtEnd().toString().replace("T", " ").length() == 16) {
                    map.put("dtEnd", m.getDtEnd().toString().replace("T", " ") + ":00");
                } else {
                    map.put("dtEnd", m.getDtEnd().toString().replace("T", " ").substring(0,19));
                }
            }
            map.put("status", m.getStatus());
            map.put("userId", m.getUserId());
            return map;
        }).collect(Collectors.toList());

    }

    @PostMapping("/user")
    public @ResponseBody
    List<Map<String, Object>> user(Authentication authentication) {

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));
        List<ScheduleLogStep> findAll = scheduleLogStepService.findByUserIdOrderByIdDesc(userName);

        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(m.getId()));
            map.put("scheduleName", m.getScheduleName());
            map.put("stepName", m.getStepName());
            if (m.getDtCommand().toString().replace("T", " ").length() == 16) {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ") + ":00");
            } else {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ").substring(0,19));
            }

            if(m.getDtStart() == null){
                map.put("dtStart", "");
            }
            else{
                if (m.getDtStart().toString().replace("T", " ").length() == 16) {
                    map.put("dtStart", m.getDtStart().toString().replace("T", " ") + ":00");
                } else {
                    map.put("dtStart", m.getDtStart().toString().replace("T", " ").substring(0,19));
                }
            }

            if(m.getDtEnd() == null){
                map.put("dtEnd", "");
            }
            else{
                if (m.getDtEnd().toString().replace("T", " ").length() == 16) {
                    map.put("dtEnd", m.getDtEnd().toString().replace("T", " ") + ":00");
                } else {
                    map.put("dtEnd", m.getDtEnd().toString().replace("T", " ").substring(0,19));
                }
            }
            map.put("status", m.getStatus());
            map.put("userId", m.getUserId());
            return map;
        }).collect(Collectors.toList());

    }

    @PostMapping("/StepLogadmin")
    public @ResponseBody
    List<Map<String, Object>> StepLogAdmin() {
        List<ScheduleLogWorkflow> findAll = scheduleLogWorkflowService.findAllByOrderByIdDesc();
        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(m.getId()));
            map.put("scheduleName", m.getScheduleName());
            map.put("idSchedule", m.getIdSchedule());
            if (m.getDtCommand().toString().replace("T", " ").length() == 16) {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ") + ":00");
            } else {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ").substring(0,19));
            }

            if(m.getDtStart() == null){
                map.put("dtStart", "");
            }
            else{
                if (m.getDtStart().toString().replace("T", " ").length() == 16) {
                    map.put("dtStart", m.getDtStart().toString().replace("T", " ") + ":00");
                } else {
                    map.put("dtStart", m.getDtStart().toString().replace("T", " ").substring(0,19));
                }
            }

            if(m.getDtEnd() == null){
                map.put("dtEnd", "");
            }
            else{
                if (m.getDtEnd().toString().replace("T", " ").length() == 16) {
                    map.put("dtEnd", m.getDtEnd().toString().replace("T", " ") + ":00");
                } else {
                    map.put("dtEnd", m.getDtEnd().toString().replace("T", " ").substring(0,19));
                }
            }
            map.put("scheduleDesc", m.getScheduleDesc());
            map.put("sequential", m.getSequential());
            map.put("type", m.getType());
            map.put("userId", m.getUserId());
            return map;
        }).collect(Collectors.toList());

    }

    @PostMapping("/StepLoguser")
    public @ResponseBody
    List<Map<String, Object>> StepLogUser(Authentication authentication) {

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));
        List<ScheduleLogWorkflow> findAll = scheduleLogWorkflowService.findByUserIdOrderByIdDesc(userName);

        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(m.getId()));
            map.put("scheduleName", m.getScheduleName());
            if (m.getDtCommand().toString().replace("T", " ").length() == 16) {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ") + ":00");
            } else {
                map.put("dtCommand", m.getDtCommand().toString().replace("T", " ").substring(0,19));
            }

            if(m.getDtStart() == null){
                map.put("dtStart", "");
            }
            else{
                if (m.getDtStart().toString().replace("T", " ").length() == 16) {
                    map.put("dtStart", m.getDtStart().toString().replace("T", " ") + ":00");
                } else {
                    map.put("dtStart", m.getDtStart().toString().replace("T", " ").substring(0,19));
                }
            }

            if(m.getDtEnd() == null){
                map.put("dtEnd", "");
            }
            else{
                if (m.getDtEnd().toString().replace("T", " ").length() == 16) {
                    map.put("dtEnd", m.getDtEnd().toString().replace("T", " ") + ":00");
                } else {
                    map.put("dtEnd", m.getDtEnd().toString().replace("T", " ").substring(0,19));
                }
            }
            map.put("scheduleDesc", m.getScheduleDesc());
            map.put("sequential", m.getSequential());
            map.put("type", m.getType());
            map.put("userId", m.getUserId());
            return map;
        }).collect(Collectors.toList());

    }

    @GetMapping("/running/{id}")
    public String singlePathVariable(@PathVariable("id") int id, Model model) {
        Schedule vo = scheduleRunService.findById((long) id).orElseGet(Schedule::new);
        List<ScheduleStep> findAll = scheduleStepService.findByIdSchedule(vo);
        model.addAttribute("steps", findAll);

        return "pathvariables/view";
    }

    @GetMapping("/history")
    public String tttt2(@PageableDefault(sort = {"id"}) Pageable pageable, Authentication authentication, Principal principal) {
        //model.addAttribute("Workflows", scheduleService.findAll());
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));

        if (userRole.equals("ROLE_ADMIN")) {
            return "schedule/schedule-logAdmin";
        } else {
            return "schedule/schedule-log";
        }
    }

    @GetMapping("/schedulelist")
    public @ResponseBody
    List<Map<String, Object>> schedulelist(@PageableDefault(sort = {"id"}) Pageable pageable, Model model, Principal principal) {
        List<Schedule> findAll = scheduleService.findAll();
        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("scheduleName", m.getScheduleName());
            return map;
        }).collect(Collectors.toList());
    }

    @PostMapping("/steps")
    public @ResponseBody
    List<Map<String, Object>> steps(@RequestBody HashMap<String, String> data) {
        List<ScheduleLogStep> findAll = scheduleLogStepService.findByIdWorkflow(Long.parseLong(data.get("workflow")));
        //System.out.println(findAll);
        return findAll.stream().map(m -> {

            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("stepName", m.getStepName());
            return map;
        }).collect(Collectors.toList());
    }
    @PostMapping("/logs")
    public @ResponseBody
    List<Map<String, Object>> logs(@RequestBody HashMap<String, String> data) {

        ScheduleLogStep s = scheduleLogStepService.findById(Long.parseLong(data.get("workflow")));
        //List<ContainerStatus> findAll2 = containerStatusService.findContainerStatus("de36f1ea3393");
        System.out.println(s);
        List<ContainerLog> findAll = containerLogService.h2o_list(s.getContainerId());
        System.out.println(findAll);
//        return findAll2.stream().map(m -> {
//
//            Map<String, Object> map = new HashMap<>();
//            System.out.println(m);
//            System.out.println(m.getContainerStatus());
//            return map;
//        }).collect(Collectors.toList());

        return findAll.stream().map(m -> {

            Map<String, Object> map = new HashMap<>();
            map.put("message", m.getMessage());
            return map;
        }).collect(Collectors.toList());
    }
}
