package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Agent;
import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStepList;
import com.asianaidt.ict.analyca.domain.schedulercore.service.AgentService;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleStepListService;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleForStompRequest;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleForStompResponse;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleServiceStateList;
import com.asianaidt.ict.analyca.webserver.service.ScheduleRunService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

//    private final ScheduleService scheduleService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AgentService agentService;
    private final ScheduleRunService scheduleRunService;


    private MessageHeaders createHeaders(@Nullable String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if (sessionId != null) headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @GetMapping("/crud")
    public String crud(@PageableDefault(sort ={"id"}) Pageable pageable, Model model, Principal principal) {
//        return "schedule/schedule-crud";
        return "schedule/schedule-crud_chain";
    }

    @GetMapping("/detail")
    public String detailGet(@PageableDefault(sort ={"id"}) Pageable pageable, Model model,
                            Principal principal, Authentication authentication,
                            @RequestBody(required = false) HashMap<String, String> data) {
        return "schedule/schedule-detail";
    }

    @PostMapping("/detail")
    public String detailPost(@PageableDefault(sort ={"id"}) Pageable pageable, Model model,
                             Principal principal, Authentication authentication,
                             @RequestParam Map<String, String> body) {
        try {
            if (body.get("ptype") != null)
                model.addAttribute("ptype", body.get("ptype").toLowerCase());
            if (body.get("scdstatus") != null)
                model.addAttribute("scdstatus", body.get("scdstatus").toLowerCase());
            if (body.get("sid") != null)
                model.addAttribute("sid", body.get("sid"));
        } catch (Exception e){
        }
        System.out.println("");

//        return "forward:schedule/schedule-detail";
        return "schedule/schedule-detail";
    }


    @GetMapping("/history")
    public String history(@PageableDefault(sort ={"id"}) Pageable pageable, Model model) {
        return "schedule/schedule-history";
    }

    @GetMapping("/running")
    public String running(@PageableDefault(sort ={"id"}) Pageable pageable, Model model, Principal principal) {
        return "schedule/schedule-running";
    }

    @Value("${temp.path}") private String tempPath;

    @PostMapping("")
    public String upload(@RequestParam String msg, @RequestParam MultipartFile[] files) throws IOException {
        log.info("Upload start : {}", msg);
        for (MultipartFile file : files) {
            File tmp = new File(tempPath + UUID.randomUUID().toString());
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(), tmp);
            } catch (IOException e) {
                log.error("Error while copying.", e);
                throw e;
            }
        }
        return "success";
    }

    @PostMapping("/userId")
    public @ResponseBody Map<String, String> user(@RequestBody HashMap<String, String> data, Authentication authentication){
        UserDetails user = (UserDetails)authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));

        Map<String, String> map = new HashMap<>();
        if(data.get("userId").equals(userName) || userRole.contains("ROLE_ADMIN")){
            map.put("access", "true");
        }
        else{
            map.put("access", "false");
        }
        return map;
    }

    public List<String> cronNextExecution(String cronExp){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss", Locale.KOREAN);

        CronSequenceGenerator cronTrigger = new CronSequenceGenerator(cronExp);
        List<String> nextList = new ArrayList<>();
        Date next = new Date();
        for(int i = 0; i < 10; i++) {
            next = cronTrigger.next(next);
            nextList.add(df.format(next));
        }
//        System.out.println(nextList);

        return nextList;
    }

    @PostMapping("/crud")
    public @ResponseBody List<Map<String, Object>> crud1() {

        List<Schedule> findAll = scheduleRunService.findByDeleted(Schedule.deletedStatus.EXIST);

        return findAll.stream().map(m -> {
//            Agent agent = agentService.findByUserId(m.getAgent());
            Map<String, Object> map = new HashMap<>();
            m.setUsed(m.getUsed());
            map.put("id", String.valueOf(m.getId()));
            map.put("scheduleName", m.getScheduleName());
//            map.put("host", agent.getIp() + "(" + agent.getHostname() + ")");
//            map.put("command", m.getCommand());
            map.put("cronExpression", m.getCronExpression());
            map.put("cronExpressionStr", m.getCronExpressionStr());
            map.put("cronNextExecution", cronNextExecution(m.getCronExpression()));
//            map.put("dtStart", m.getDtStart().toString().replace("T", " ") + " ~ 9999-12-31 23:59");
            //map.put("dtEnd", m.getDtEnd().toLocalDate());
            map.put("scheduleDesc", m.getScheduleDesc());
            map.put("type", m.getType());
            map.put("sequential", m.getSequential());
            map.put("userId", m.getUserId());
            map.put("used", m.getUsed());
            return map;
        }).collect(Collectors.toList());
    }

    @PostMapping("/add")
    public @ResponseBody void add(@RequestParam("file") MultipartFile uploadfile,
                                  @RequestParam("cronExpression") String cronExpression,
                                  @RequestParam("dtStart") String dtStart,
                                  @RequestParam("type") String type,
                                  @RequestParam("scheduleDesc") String scheduleDesc,
                                  @RequestParam("scheduleName") String scheduleName,
                                  @RequestParam("host") String host,
                                  Principal principal) throws Exception{

        try {
            String filePath = tempPath + File.separator + uploadfile.getOriginalFilename();

            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            stream.write(uploadfile.getBytes());
            stream.close();

            String[] hostSplit = host.split("\\(");

            Schedule vo = new Schedule();
            vo.setScheduleName(scheduleName);
            vo.setType(type);
            vo.setUserId("leekn");
            vo.setScheduleDesc(scheduleDesc);
//            Agent agent = agentService.findByIpAndHostname(hostSplit[0], hostSplit[1].substring(0, hostSplit[1].length()-1));
//            vo.setAgent(agent.getUserId());
            vo.setUsed(Schedule.usedStatus.RUNNING);
            vo.setDeleted(Schedule.deletedStatus.EXIST);
//            vo.setSequential(Schedule.sequentialStatus.CONCURRENT);
            vo.setSequential(Schedule.sequentialStatus.PARALLEL);
//            vo.setCommand(uploadfile.getOriginalFilename());
            vo.setCronExpression(cronExpression);
            System.out.println(dtStart);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dtStart, formatter);
            vo.setDtStart(dateTime);
            vo.setDtEnd(LocalDate.parse("9999-12-31").atTime(0, 0,0));

            scheduleRunService.scheduleAdd(vo);
        }
        catch (Exception e){

        }
    }

    @PutMapping("/crud")
    public @ResponseBody void updateScheduler(@RequestParam("id") String id,
                                              @RequestParam("file") MultipartFile uploadfile,
                                              @RequestParam("cronExpression") String cronExpression,
                                              @RequestParam("dtStart") String dtStart,
                                              @RequestParam("type") String type,
                                              @RequestParam("scheduleDesc") String scheduleDesc,
                                              @RequestParam("scheduleName") String scheduleName,
                                              @RequestParam("host") String host,
                                              Principal principal) throws Exception{
        try {
            Schedule vo = scheduleRunService.findById(Long.parseLong(id)).orElseGet(Schedule::new);

            if(uploadfile.getOriginalFilename().length() != 0){
                String filePath = tempPath + File.separator + uploadfile.getOriginalFilename();

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(uploadfile.getBytes());
                stream.close();

//                vo.setCommand(uploadfile.getOriginalFilename());
            }

            String[] hostSplit = host.split("\\(");
            vo.setScheduleName(scheduleName);
            vo.setType(type);
            vo.setUserId("leekn");
            vo.setScheduleDesc(scheduleDesc);
            //vo.setHostName(hostSplit[1].substring(0, hostSplit[1].length()-1));
            //vo.setHostIp(hostSplit[0]);
//            Agent agent = agentService.findByIpAndHostname(hostSplit[0], hostSplit[1].substring(0, hostSplit[1].length()-1));
//            vo.setAgent(agent.getUserId());
            vo.setCronExpression(cronExpression);
            System.out.println(dtStart);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dtStart, formatter);
            vo.setDtStart(dateTime);
            //vo.setDtEnd(LocalDate.parse(dtEnd).atTime(0, 0,0));

            scheduleRunService.scheduleUpdate(vo);
        }
        catch (Exception e){

        }
    }
    @DeleteMapping("/crud")
    public @ResponseBody void deleteScheduler(@RequestBody HashMap<String, String> data) throws Exception {
        Schedule vo = scheduleRunService.findById(Long.parseLong(data.get("id"))).orElse(new Schedule());
//        vo.setDeleted(Schedule.deletedStatus.DELETED);
//        scheduleService.save(vo);
        scheduleRunService.scheduleDelete(vo);
    }



    @PutMapping("/used")
    public @ResponseBody void updateUsed(@RequestBody HashMap<String, String> data) throws Exception {
        System.out.println(data);
        Schedule vo = scheduleRunService.findById(Long.parseLong(data.get("id"))).orElse(new Schedule());
//        if(data.get("used").equals("RUNNING")){
        if(vo.getUsed() == Schedule.usedStatus.RUNNING) {
            vo.setUsed(Schedule.usedStatus.STOP);
            scheduleRunService.schedulePause(vo);
        }
        else{
            vo.setUsed(Schedule.usedStatus.RUNNING);
            scheduleRunService.scheduleResume(vo);
        }

//        scheduleService.save(vo);
    }

    @PostMapping("/hostlist")
    public @ResponseBody List<Map<String, Object>> list() {
        List<Agent> findAll = agentService.findAll();
        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("host", m.getIp() +"(" +m.getHostname() + ")");
            map.put("ip", m.getIp());
            map.put("name", m.getHostname());
            map.put("agent", m.getUserId());
            return map;
        }).collect(Collectors.toList());
    }

    @MessageMapping("/service/add")
    public void addService(ScheduleForStompResponse res) {
        // 실행중인 서비스 테이블에 pid를 저장
//        System.out.println("res = " + res);
//        scheduleRunService.saveProcess(res);
    }

    @MessageMapping("/service/stateList")
    public void responseServiceState(ScheduleServiceStateList stateList) {
//        System.out.println("stateList = " + stateList);
//        scheduleRunService.updateStateList(stateList);
    }

//    @MessageMapping("/service/add")
//    public void getContainerId(ScheduleForStompResponse res) {
//        // 실행중인 서비스 테이블에 pid를 저장
////        System.out.println("res = " + res);
////        scheduleRunService.saveProcess(res);
//
//    }


    @PostMapping("/service/run")
    public @ResponseBody void execute(@RequestBody HashMap<String, String> data) {
        scheduleRunService.scheduleRun(Long.parseLong(data.get("id")));
    }



    public Boolean userCheck(String userId, Authentication authentication){
        UserDetails user = (UserDetails)authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));

        if (userId.equals(userName) || userRole.equals("ROLE_ADMIN")) {
            return true;
        } else {
            return false;
        }
    }
}
