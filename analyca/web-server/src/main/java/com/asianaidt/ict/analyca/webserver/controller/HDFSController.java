package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.securitycore.model.Member;
import com.asianaidt.ict.analyca.domain.securitycore.service.MemberService;
import com.asianaidt.ict.analyca.webserver.service.HDFSBrowserService;
import com.google.gson.Gson;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hdfs")
public class HDFSController {

    HDFSBrowserService hdfsBrowserService;

    public HDFSController(HDFSBrowserService hdfsBrowserService) {
        this.hdfsBrowserService = hdfsBrowserService;
    }

    @GetMapping("/monitor")
    public String monitor() {
        return "hdfs/monitor";
    }

    @GetMapping("/monitor-datanodes")
    public String hdfsMonitor() {
        return "hdfs/monitor-datanodes";
    }

    @GetMapping("/filebrowser")
    public String browser() {
        return "hdfs/filebrowser";
    }

    @RequestMapping(value = {"/files/**"})
    public @ResponseBody
    String files(@RequestParam(value = "op") String op,
                 @RequestParam(value = "flag") String flag,
                 Authentication authentication) throws UnsupportedEncodingException {
        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        String requestedValue = builder.buildAndExpand().getPath();  // I want this.
        String hdfsPath = URLDecoder.decode(Objects.requireNonNull(requestedValue).replaceAll("/hdfs/files/", "/"), "utf8");

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));

        switch (op) {
            // 파일 탐색
            case "BROWSING":
                return new Gson().toJson(hdfsBrowserService.getFileList(hdfsPath, userName, userRole, flag));
            // 파일 생성
            case "MKDIR":
                return new Gson().toJson(hdfsBrowserService.createDirectory(hdfsPath, userName, userRole));
            case "DELETE":
                return new Gson().toJson(hdfsBrowserService.deletePath(hdfsPath, true, userName, userRole));
            case "READ":
                return new Gson().toJson(hdfsBrowserService.getFileContent(hdfsPath, userName, userRole));
            case "PERMISSION":
                return new Gson().toJson(hdfsBrowserService.setPermission(hdfsPath, userName, userRole, flag));
            default:
                return null;
        }
    }

    @RequestMapping(value = {"/rename/**"})
    public @ResponseBody
    String rename(@RequestParam(value = "op") String op,
                  @RequestParam(value = "destination") String destination,
                  Authentication authentication) throws UnsupportedEncodingException {
        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        String requestedValue = builder.buildAndExpand().getPath();  // I want this.
        String hdfsPath = URLDecoder.decode(Objects.requireNonNull(requestedValue).replaceAll("/hdfs/rename/", "/"), "utf8");

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));


        return new Gson().toJson(hdfsBrowserService.rename(hdfsPath, destination, userName, userRole));

    }

    @PostMapping("/upload")
    public @ResponseBody
    String fileUpLoad(@RequestParam("files") MultipartFile[] uploadfiles,
                      @RequestParam("currentPath") String path,
                      Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));
//        String uploadedFileName = Arrays.stream(uploadfiles)
//                .map(x -> x.getOriginalFilename())
//                .filter(x -> !StringUtils.isEmpty(x))
//                .collect(Collectors.joining(" , "));

        return new Gson().toJson(hdfsBrowserService.saveUpLoadFiles(Arrays.asList(uploadfiles), path, userName, userRole));
    }

    @RequestMapping(value = {"/download/**"})
    public ResponseEntity<Resource> download(@RequestParam(value = "op") String op,
                                             Authentication authentication) throws UnsupportedEncodingException {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String userName = user.getUsername();
        String userRole = user.getAuthorities().stream().map(s -> String.valueOf(s)).collect(Collectors.joining(","));
        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        String requestedValue = builder.buildAndExpand().getPath();  // I want this.

        String hdfsPath = URLDecoder.decode(Objects.requireNonNull(requestedValue).replaceAll("/hdfs/download/", "/"), "utf8");
        if (op.equals("DOWNLOAD")) {
            Resource resource = hdfsBrowserService.downloadFile(hdfsPath, userName, userRole);

            String[] arrPath = hdfsPath.split("/");
            String hdfsFileName = arrPath[arrPath.length - 1];

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + hdfsFileName + "\"")
                    .body(resource);
        } else {
            return null;
        }
    }


}
