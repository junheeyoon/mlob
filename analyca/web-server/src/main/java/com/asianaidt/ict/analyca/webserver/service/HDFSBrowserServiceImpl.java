package com.asianaidt.ict.analyca.webserver.service;

import com.asianaidt.ict.analyca.service.hadoopservice.HdfsFileSystemController;
import lombok.AllArgsConstructor;
import org.apache.hadoop.fs.FileStatus;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class HDFSBrowserServiceImpl implements HDFSBrowserService {
    final HdfsFileSystemController hdfsFileSystemController;

    @Override
    public HashMap<String, Object> getFileList(String path, String userName, String userRole, String flag) {
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            HashMap<String, Object> fileStatusResultMap = hdfsFileSystemController.getFileList(path, userName, userRole, flag);
            FileStatus[] fileStatuses = (FileStatus[]) fileStatusResultMap.get("fileStatus");

            List<HashMap<String, Object>> fileStatusList = new ArrayList<>();

            String permissionRe = "";
            String permission = "";

            for (FileStatus fileStatus : fileStatuses) {
                HashMap<String, Object> fileListMap = new HashMap<>();
                fileListMap.put("blockSize", fileStatus.getBlockSize());
                fileListMap.put("modification_time", fileStatus.getModificationTime());
                fileListMap.put("owner", fileStatus.getOwner());
                fileListMap.put("group", fileStatus.getGroup());
                fileListMap.put("name", fileStatus.getPath().getName());
                fileListMap.put("path", fileStatus.getPath().getParent().toString());
                permission = fileStatus.getPermission().toString();
                if (permission.substring(8,9).equals("t")) {
                    permissionRe = permission.substring(0,8) + "xt";
                } else if (permission.substring(8,9).equals("T")){
                    permissionRe = permission.substring(0,8) +"-t";
                } else {
                    permissionRe = permission;
                }
                fileListMap.put("permission", permissionRe);
                fileListMap.put("sticky", fileStatus.getPermission().getStickyBit());
                fileListMap.put("type", fileStatus.isDirectory() ? "DIRECTORY" : "FILE");
                fileListMap.put("size", fileStatus.getLen());

                fileStatusList.add(fileListMap);
            }

            resultMap.put("result", "success");
            resultMap.put("msg", "");
            resultMap.put("data", fileStatusList.toArray());
            resultMap.put("currentPath", fileStatusResultMap.get("currentPath"));

            return resultMap;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
            resultMap.put("msg", e.toString());
            return resultMap;
        }
    }

    @Override
    public HashMap<String, String> createDirectory(String path, String userName, String userRole) {
        HashMap<String, String> resultMap = new HashMap<>();
        String resultString = hdfsFileSystemController.createDirectory(path, userName, userRole);
        if (resultString.equals("Create Directory Success!")) {
            resultMap.put("result", "success");
        } else {
            resultMap.put("result", "fail");
        }
        resultMap.put("msg", resultString);

        return resultMap;
    }

    @Override
    public HashMap<String, String> deletePath(String path, boolean recursive, String userName, String userRole) {
        HashMap<String, String> resultMap = new HashMap<>();
        String resultString = hdfsFileSystemController.deletePath(path, recursive, userName, userRole);

        if (resultString.equals("success")) {
            resultMap.put("result", "success");
        } else {
            resultMap.put("result", "fail");
        }
        resultMap.put("msg", resultString);

        return resultMap;
    }

    @Override
    public HashMap<String, String> getFileContent(String path, String userName, String userRole) {
        return hdfsFileSystemController.getFilePreContent(path, userName, userRole);
    }

    @Override
    public HashMap<String, String> setPermission(String path, String userName, String userRole, String permission) {
        HashMap<String, String> resultMap = new HashMap<>();

        String resultMsg = hdfsFileSystemController.setPermission(path, userName, userRole, permission);

        if (resultMsg.equals("success")) {
            resultMap.put("result", "success");
        } else {
            resultMap.put("result", "fail");
        }
        resultMap.put("msg", resultMsg);

        return resultMap;
    }

    @Override
    public HashMap<String, String> rename(String sourcePath, String destinationPath, String userName, String userRole) {
        return hdfsFileSystemController.rename(sourcePath, destinationPath, userName, userRole);
    }

    @Override
    public HashMap<String, String> setOwner(String path, String userName, String userRole) {
        HashMap<String, String> resultMap = new HashMap<>();
        String resultMsg = hdfsFileSystemController.setOwner(path, userName, userRole);

        if (resultMsg.equals("success")) {
            resultMap.put("result", "success");
        } else {
            resultMap.put("result", "fail");
        }
        resultMap.put("msg", resultMsg);

        return resultMap;
    }

    @Override
    public InputStreamResource downloadFile(String path, String userName, String userRole) {
        return hdfsFileSystemController.fileDownload(path, userName, userRole);
    }

    @Override
    public HashMap<String, String> saveUpLoadFiles(List<MultipartFile> files, String path, String userName, String userRole) {
        HashMap<String, String> finalResultMap = new HashMap<>();
        finalResultMap.put("result", "success");

        for (MultipartFile file : files) {
            HashMap<String, String> resultMap = hdfsFileSystemController.saveFileToHDFS(file, path, userName, userRole);
            if (resultMap.get("result").equals("fail")) {
                finalResultMap.put("result", "fail");
                finalResultMap.put("data", resultMap.get("data"));
            }
        }
        return finalResultMap;
    }

}
