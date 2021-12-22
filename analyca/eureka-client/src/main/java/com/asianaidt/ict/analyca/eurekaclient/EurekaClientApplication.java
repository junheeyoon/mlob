package com.asianaidt.ict.analyca.eurekaclient;

import com.asianaidt.ict.analyca.eurekaclient.domain.Crawler;
import com.asianaidt.ict.analyca.eurekaclient.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class EurekaClientApplication {

    @Autowired
    private CrawlerService crawlerService;

    @GetMapping("/eureka-test")
    public List<Map<String, Object>> test() {

        List<Crawler> findAll = crawlerService.findAll();
        return findAll.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            //map.put("idCrawler", String.valueOf(m.getIdCrawler()));
            //map.put("channel", m.getChannel());
            //map.put("searchKeyword", m.getSearchKeyword());
            //map.put("contentTitle", m.getContentTitle());
            //map.put("contentText", m.getContentText());
            map.put("contentText", crawlerService.konlpy(m.getContentText()));
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/test")
    public String test1() {

        return "hello";
    }



    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

}
