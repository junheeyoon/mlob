package com.asianaidt.ict.analyca.service.hadoopservice;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;

@Service
public class HdfsFileSystemController {
    final HdfsFileSystemConfigure hdfsFileSystemConfigure;

    Configuration conf = new Configuration();

    public HdfsFileSystemController(HdfsFileSystemConfigure hdfsFileSystemConfigure) {
        this.hdfsFileSystemConfigure = hdfsFileSystemConfigure;
        FileSystem.setDefaultUri(conf, hdfsFileSystemConfigure.getUri());
    }

    private FileSystem getUserFileSystem(String userName, String userRole) throws IOException, InterruptedException {
        String permissionUser = userRole.equals("ROLE_ADMIN") ? "platform" : userName;
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser(permissionUser);
        return ugi.doAs(new PrivilegedExceptionAction<FileSystem>() {
            @Override
            public FileSystem run() throws Exception {
                return FileSystem.get(conf);
            }
        });
    }

    public HashMap<String, Object> getFileList(String path, String userName, String userRole, String flag) throws IOException, InterruptedException {
        HashMap<String, Object> resultMap = new HashMap<>();

        FileSystem hdfsFS = getUserFileSystem(userName, userRole);
        String routePath = flag.equals("FIRST") ? ("/user/" + userName + "/") : path;

        FileStatus[] fileStatuses = hdfsFS.listStatus(new Path(routePath));
        resultMap.put("fileStatus", fileStatuses);
        resultMap.put("currentPath", routePath);

        hdfsFS.close();

        return resultMap;
    }

    public String deletePath(String path, boolean recursive, String userName, String userRole) {
        try {
            FileSystem hdfsFS = getUserFileSystem(userName, userRole);
            hdfsFS.delete(new Path(path), recursive);

            hdfsFS.close();
            return "success";
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public String setOwner(String path, String userName, String userRole) {
        try {
            FileSystem hdfsFS = getUserFileSystem(userName, userRole);
            hdfsFS.setOwner(new Path(path), userName, userName);
            hdfsFS.close();
            return "success";
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public String setPermission(String path, String userName, String userRole, String permission) {
        try {
            String permissionRe = permission.substring(0,9);
            short permisssionShort;
            if (permission.substring(9,10).equals("t")) {
                permisssionShort = (short) (FsPermission.valueOf("-" + permissionRe).toShort() | 01000);
            } else {
                permisssionShort = (FsPermission.valueOf("-" + permissionRe).toShort());
            }
            FileSystem hdfsFS = getUserFileSystem(userName, userRole);

            hdfsFS.setPermission(new Path(path), new FsPermission(permisssionShort));
            hdfsFS.close();
            return "success";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public String createDirectory(String path, String userName, String userRole) {
        FileSystem hdfsFS = null;
        String resultMsg = "";
        try {
            hdfsFS = getUserFileSystem(userName, userRole);

            if (!hdfsFS.exists(new Path(path))) {
                hdfsFS.mkdirs(new Path(path));
                resultMsg = "Create Directory Success!";
            } else {
                resultMsg = path + " is Exist";
            }

            hdfsFS.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            resultMsg = e.toString();
        }

        return resultMsg;
    }

    public HashMap<String, String> rename(String sourcePath, String destinationPath, String userName, String userRole) {
        HashMap<String, String> resultMap = new HashMap<>();
        try {
            FileSystem hdfsFS = getUserFileSystem(userName, userRole);
            if (hdfsFS.rename(new Path(sourcePath), new Path(destinationPath))) {
                resultMap.put("result", "success");
                resultMap.put("msg", "OK");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
            resultMap.put("msg", e.toString());
        }

        return resultMap;
    }

    public InputStreamResource fileDownload(String path, String userName, String userRole) {
        try {
            FileSystem hdfsFS = getUserFileSystem(userName, userRole);
            FSDataInputStream in = hdfsFS.open(new Path(path));
            return new InputStreamResource(in);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> saveFileToHDFS(MultipartFile file, String path, String userName, String userRole) {
        HashMap<String, String> resultMap = new HashMap<>();

        try {
            FileSystem hdfsFS = getUserFileSystem(userName, userRole);

            OutputStream outputStream = hdfsFS.create(new Path(path + file.getOriginalFilename()));
            IOUtils.copyBytes(file.getInputStream(), outputStream, conf);

            hdfsFS.close();

            resultMap.put("result", "success");
            resultMap.put("data", "OK");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
            resultMap.put("data", e.toString());
        }

        return resultMap;
    }

    /**
     * get HDFS File Content
     *
     * @param path
     * @return
     */
    public HashMap<String, String> getFilePreContent(String path, String userName, String userRole) {
        HashMap<String, String> resultMap = new HashMap<>();
        BufferedReader br = null;
        FSDataInputStream in = null;
        FileSystem hdfsFS = null;
        try {
            hdfsFS = getUserFileSystem(userName, userRole);

            ContentSummary fsStatus = hdfsFS.getContentSummary(new Path(path));
            long filelength = fsStatus.getLength();

            in = hdfsFS.open(new Path(path));

            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            char[] chbuf = new char[2048];

            if (filelength > 2048) {
                br.read(chbuf, 0, 2048);
            } else {
                br.read(chbuf, 0, (int) filelength);
            }

            String preContent = new String(chbuf);

            resultMap.put("result", "success");
            resultMap.put("data", preContent);
            return resultMap;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
            resultMap.put("data", e.toString());
            return resultMap;
        } finally {
            try {
                if (br != null) br.close();
                if (in != null) in.close();
                if (hdfsFS != null) hdfsFS.close();
            } catch (IOException e) {

            }
        }
    }
}
