package com.asianaidt.ict.analyca.webserver.controller;


//import com.asianaidt.ict.analyca.service.sparkservice.HiveQueryServiceExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Controller
@RequestMapping("/analysis")
public class AnalysisController {

//    private final HiveQueryServiceExecutor hiveQueryServiceExecutor;
//
//    @GetMapping("/main")
//    public String browser() {
//        return "analysis/analysis-hive";
//    }
//
//    @PostMapping("/databases")
//    public @ResponseBody List<Map<String, Object>> getDatabases() {
//        return hiveQueryServiceExecutor.getDatabases();
//    }
//
//    @PostMapping("/tables/{database}")
//    public @ResponseBody List<Map<String, Object>> getTables(@PathVariable(required = false) String database) {
//        return hiveQueryServiceExecutor.getTables(database);
//    }
}
