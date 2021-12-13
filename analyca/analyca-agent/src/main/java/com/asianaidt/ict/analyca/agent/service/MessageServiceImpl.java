package com.asianaidt.ict.analyca.agent.service;

import com.asianaidt.ict.analyca.agent.stomp.StompClient;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleForStompRequest;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleForStompResponse;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleServiceStateList;
import com.asianaidt.ict.analyca.system.websocketcore.dto.ScheduleServiceStateRequest;
import com.asianaidt.ict.analyca.system.websocketcore.type.ScheduleServiceState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageServiceImpl implements MessageService {
    private final StompClient stompClient;

//    private final StompSession stompSession;

    private static String OS = System.getProperty("os.name").toLowerCase();
    @Value("${temp.path}")
    private String tempPath;

    @Lazy
    public MessageServiceImpl(StompClient stompClient) {
        this.stompClient = stompClient;
    }
//    public MessageServiceImpl(StompSession stompSession) {
//        this.stompSession = stompSession;
//    }

    @Async("threadPoolTaskExecutor")
    @Override
    public void startCommand(ScheduleForStompRequest request) throws Exception {
        final String WRITE_FILE_PATH = "/Users/junhee/schedule/";

        byte[] file = request.getFile();
        // save byte[] into a file
        Path path = Paths.get(tempPath + File.separator + request.getFilename());

        System.out.println(tempPath + File.separator + request.getFilename());
        Files.write(path, file);

        try {
            long pid = -1;
            String line;
            Process p = null;
            if (OS.contains("windows")) {
                p = Runtime.getRuntime().exec("cmd /c " + tempPath + File.separator + request.getFilename());
//                pid = p.processHandle.pid();
                Pattern pat = Pattern.compile("pid=([0-9]+)");
                Matcher m = pat.matcher(p.toString());
                if (m.find()) {
                    pid = Long.parseLong(m.group(1));
                }
            } else {
//                p = Runtime.getRuntime().exec("sh " + tempPath + File.separator + request.getFilename());
                ProcessBuilder builder = new ProcessBuilder("sh", tempPath + File.separator + request.getFilename());
                p = builder.start();
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
            }
            System.out.println("id of the thread is " + pid);
            //Thread.sleep(20000);
//            stompSession.send("/analyca/service/add", new ScheduleForStompResponse(request.getScheduleId(), pid));
            stompClient.send("/analyca/service/add", new ScheduleForStompResponse(request.getScheduleId(), pid));
        } catch (Exception err) {
            err.printStackTrace();
        } finally {

        }
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public void statusCommand(ScheduleServiceStateList list) throws Exception {

        try {

            for (int i = 0; i < list.getRequestList().size(); i++) {

                ScheduleServiceStateRequest scheduleServiceStateRequest = list.getRequestList().get(i);
                Process p = null;
                if (OS.contains("windows")) {
                    p = Runtime.getRuntime().exec("tasklist /svc /FI \"PID eq " + scheduleServiceStateRequest.getPid() + "\"");
                } else {
                    p = Runtime.getRuntime().exec("ps -f " + scheduleServiceStateRequest.getPid());
                }

                BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader bro = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line;
                int j = 0;
                String result = null;
                while ((line = bri.readLine()) != null) {
                    j += 1;
                    if (j == 2) {
                        result = line;
                    }
                }
                while ((line = bro.readLine()) != null) {
                    System.out.println(line);
                }
                if (result == null) {
                    scheduleServiceStateRequest.setState(ScheduleServiceState.STOP);
                    System.out.println("done");
                } else {
                    scheduleServiceStateRequest.setState(ScheduleServiceState.RUN);
                    System.out.println("running");
                }
                bri.close();
                bro.close();
                p.getErrorStream().close();
                p.getInputStream().close();
                p.getOutputStream().close();
                p.waitFor();

            }

//            stompSession.send("/analyca/service/stateList", list);
            stompClient.send("/analyca/service/stateList", list);
            //

        } catch (Exception err) {
            err.printStackTrace();
        } finally {

        }
    }

}
