package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.schedulercore.model.Schedule;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStep;
import com.asianaidt.ict.analyca.domain.schedulercore.model.ScheduleStepList;
import com.asianaidt.ict.analyca.domain.schedulercore.service.ScheduleStepListService;
import com.asianaidt.ict.analyca.webserver.service.ScheduleRunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/step")
public class ScheduleStepController {

    private final ScheduleStepListService stepListService;
    private final ScheduleRunService scheduleRunService;

    @PostMapping({"/list", "/list/{id}"})
    public @ResponseBody List<Map<String, Object>> steplist(@PathVariable(required = false) String id) {
        List<ScheduleStepList> findAll = new ArrayList<>();
        if(id != null) findAll.add(stepListService.findById(Long.parseLong(id)).orElse(new ScheduleStepList()));
        else findAll = stepListService.findAll();

        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
//            map.put("host", m.getIp() +"(" +m.getHostname() + ")");
            map.put("id", m.getId());
            map.put("image", m.getImage());
            map.put("tag", m.getTag());
            map.put("containerName", m.getContainerName());
            map.put("port", m.getPort());
            map.put("volume", m.getVolume());
            map.put("memory", m.getMemory());
            map.put("cpu", m.getCpu());
            map.put("gpu", m.getGpu());
            map.put("options", m.getOptions());
            map.put("commands", m.getCommands());
            return map;
        }).collect(Collectors.toList());
    }


    @PostMapping("/workflowDetail/{id}")
//    public @ResponseBody List<Map<String, Object>> workflowDetail(@PathVariable(required = false) String id) {
    public @ResponseBody Map<String,Object> workflowDetail(@PathVariable(required = false) String id) {
        List<ScheduleStep> findAll = new ArrayList<>();
        Optional<Schedule> ifSchedule = scheduleRunService.findById(Long.parseLong(id));
        if(!ifSchedule.isPresent()){
            return null;
        }
        Schedule schedule = ifSchedule.get();
        findAll = scheduleRunService.findStepsByIdSchedule(schedule);

        Map<String, Object> result = new HashMap<>();
        result.put("schedule", schedule);
        result.put("steps", findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
//            map.put("id", m.getId());
//            map.put("idSchedule", m.getIdSchedule());
            map.put("stepOrder", m.getStepOrder());
            map.put("stepName", m.getStepName());
            map.put("stepDesc", m.getStepDesc());
            map.put("idStepList", m.getIdStepList());
            map.put("agent", m.getAgent());
            map.put("host", m.getHost());
            map.put("image", m.getImage());
            return map;
        }).collect(Collectors.toList()));

        return result;
    }

    /**
     * container distribute 페이지에서 스텝 세팅 저장
     * */
    @PostMapping("/save")
    public @ResponseBody void save(@RequestBody HashMap<String, String> data) {
        ScheduleStepList step = new ScheduleStepList();
        step.setImage(data.get("imageName"));
        step.setTag(data.get("tag"));
        step.setContainerName(data.get("containerName"));
        step.setPort(data.getOrDefault("port", ""));
        step.setVolume(data.getOrDefault("volume", ""));
        step.setMemory(data.getOrDefault("memory", ""));
        step.setCpu(data.getOrDefault("cpu", ""));
        step.setGpu(data.getOrDefault("gpu", "false"));
        step.setOptions(data.getOrDefault("options", ""));
        step.setCommands(data.getOrDefault("commands", ""));

//        step.setGpu(true);
        stepListService.save(step);
    }

    /**
     * 스케줄의 스텝 정보 저장
     * */
    @PostMapping("/create")
    public @ResponseBody Map<String, Object> createSchedule(@RequestBody HashMap<String, String> data, Authentication authentication) {
        Schedule scd = new Schedule();
        Boolean modifyFlag = false;

        // 스케줄 수정
        if (data.get("idSchedule") != null && !data.get("idSchedule").equals("")){
            modifyFlag = true;
            scd = scheduleRunService.findById(Long.parseLong(data.get("idSchedule"))).orElse(new Schedule());
            scd.setUsed(scd.getUsed());
        } else {    // 스케줄 신규 생성
            scd.setUsed(Schedule.usedStatus.RUNNING);
        }

        scd.setScheduleName(data.get("scheduleName"));
        scd.setScheduleDesc(data.get("scheduleDesc"));
        scd.setScheduleDesc(data.get("scheduleDesc"));
        if(!data.get("cronExpression").equals("")) {
            scd.setCronExpression(data.get("cronExpression"));
            scd.setCronExpressionStr(data.get("cronExpressionStr"));
        }
        scd.setType(data.get("type"));
        if (data.get("parallel").equalsIgnoreCase("parallel") || data.get("parallel").equalsIgnoreCase("concurrent"))
//            scd.setSequential(Schedule.sequentialStatus.CONCURRENT);
            scd.setSequential(Schedule.sequentialStatus.PARALLEL);
        else
            scd.setSequential(Schedule.sequentialStatus.SEQUENTIAL);
        scd.setUserId(authentication.getName());

        scd.setDeleted(Schedule.deletedStatus.EXIST);

        Boolean saveFlag = false;
        Schedule saved = new Schedule();
        try {
            saved = scheduleRunService.save(scd);
//        Long sid = saved.getId();
            saveFlag = scheduleRunService.saveSteps(saved, data.get("steps"));
            if(modifyFlag)
                scheduleRunService.scheduleUpdate(saved);
            else
                scheduleRunService.scheduleAdd(saved);
        } catch (Exception e){
        }

        Map<String, Object> result = new HashMap<>();
        result.put("result", saveFlag);
        return result;
    }

    /**
     * 특정 스케줄에 대한 스텝 정보 삭제
    * */
    @PostMapping("/delete")
    public @ResponseBody void deleteStepsBySchedule(@RequestBody HashMap<String, String> data, Authentication authentication) {

    }


}
