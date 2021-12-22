package com.asianaidt.ict.analyca.webserver.service;


import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface HDFSBrowserService {
    HashMap<String, Object> getFileList(String path, String userName, String userRole, String flag);

    HashMap<String, String> createDirectory(String path, String userName, String userRole);

    HashMap<String, String> deletePath(String path, boolean recursive, String userName, String userRole);

    HashMap<String, String> getFileContent(String path, String userName, String userRole);

    HashMap<String, String> setPermission(String path, String userName, String userRole, String permission);

    HashMap<String, String> setOwner(String path, String userName, String userRole);

    InputStreamResource downloadFile(String path, String userName, String userRole);

    HashMap<String, String> saveUpLoadFiles(List<MultipartFile> files, String path, String userName, String userRole);

    HashMap<String, String> rename(String sourcePath, String destinationPath, String userName, String userRole);
}
