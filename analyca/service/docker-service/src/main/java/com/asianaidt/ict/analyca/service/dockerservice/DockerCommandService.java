package com.asianaidt.ict.analyca.service.dockerservice;

import com.asianaidt.ict.analyca.system.dockercore.dto.DockerCommandStompResponse;
import com.asianaidt.ict.analyca.system.dockercore.dto.DockerContainerResources;
import com.asianaidt.ict.analyca.system.dockercore.util.UnitConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.*;

@Slf4j
@Service
public class DockerCommandService {
    /**
     * 컨테이너의 정보를 수집하는 함수이다.
     * {@link #stats(DockerContainerResources)}, {@link #ps(DockerContainerResources)}를 사용하여 정보를 병합하는데
     * 실행(running)중인 컨테이너는 리소스 정보가 있으나 중지(exited)된 컨테이너는 리소스 정보가 없다.
     *
     * @return 실행 및 중지 된 컨테이너 정보 리스트
     * @author leekn
     * @version 1.0.0 20/5/13
     * @see #stats(DockerContainerResources)
     * @see #ps(DockerContainerResources)
     */
    public DockerContainerResources metrics() {
        DockerContainerResources containerResources = new DockerContainerResources();
        stats(containerResources);
        ps(containerResources);
        return containerResources;
    }

    /***
     * 도커 컨테이너의 리소스 상태를 반환하는 함수이다.
     * 실행중인 도커의 리소스만 확인이 가능하며 다음과 같은 커맨드라인 명령어를 사용한다.
     *      docker stats --no-stream --format={{.Name}}|{{.ID}}|{{.CPUPerc}}|{{.MemUsage}}|{{.NetIO}}|{{.BlockIO}}
     */
    public void stats(DockerContainerResources containerResources) {
        ProcessBuilder processBuilder = new ProcessBuilder(DOCKER, STATS, STATS_NOSTREAM, STATS_FORMAT);
        try {
            Process process = processBuilder.start();
            new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
                    .map(line -> line.split(FORMAT_DELIMITER))
                    .forEach(split -> {
                        DockerContainerResources.DockerContainerResource containerResource =
                                containerResources.getOrDefault(split[0], new DockerContainerResources.DockerContainerResource());
                        containerResource.setContainerName(split[0]);
                        containerResource.setContainerId(split[1]);
                        containerResource.setCpu(UnitConverter.toDouble(split[2]));
                        containerResource.setMem(UnitConverter.toByte(split[3], "/"));
                        containerResource.setNetIO(UnitConverter.toByte(split[4], "/"));
                        containerResource.setBlockIO(UnitConverter.toByte(split[5], "/"));
                        containerResources.put(split[0], containerResource);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 도커의 상세 정보를 반환하는 함수로 컨테이너 아이디를 통해 상세 정보를 도커 클라이언트로 부터 얻어온다.
     * 컨테이너 상세 정보 커맨드는 다음과 같다.
     *      docker inspect --format
     *      {{.Name}}|
     *      {{.Id}}|
     *      {{.HostConfig.NanoCpus}}|
     *      {{.HostConfig.Memory}}|
     *      {{.State.StartedAt}}|
     *      {{.State.Status}}|
     *      {{.Config.Image}}|
     *      {{.Config.Labels.Creator}}
     *      {{.HostConfig.Binds}}|
     *      {{range $p, $conf := .HostConfig.PortBindings}} {{$p}}:{{(index $conf 0).HostPort}} {{end}} containerId
     *
     * @param containerResources 컨테이너 리소스 관리 객체
     */
    public void ps(DockerContainerResources containerResources) {
        getContainerIdList().forEach(containerId -> {
            try {
                Process process = new ProcessBuilder(DOCKER, INSPECT, INSPECT_FORMAT, containerId).start();
                new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
                        .map(line -> line.split(FORMAT_DELIMITER))
                        .forEach(split -> {
                            String containerName = split[0].substring(1);
                            DockerContainerResources.DockerContainerResource containerResource =
                                    containerResources.getOrDefault(containerName, new DockerContainerResources.DockerContainerResource());
                            containerResource.setContainerName(containerName);
                            containerResource.setContainerId(split[1]);
                            containerResource.setCpuCnt(split[2]);
                            containerResource.setMemCnt(split[3]);
                            containerResource.setCreatedAt(split[4]);
                            containerResource.setStatus(split[5]);
                            containerResource.setImage(split[6]);
                            if (false) {
                                // inspect format에 creator가 들어갈 경우 다시 살리도록
                                containerResource.setCreator(split[7]);
                                containerResource.setBinds(split[8]);
                                if (split.length > 9) containerResource.setPorts(split[9]);
                            } else {
                                // inspect format에 creator가 없을 경우
                                containerResource.setCreator("leekn");
                                containerResource.setBinds(split[7]);
                                if (split.length > 8) containerResource.setPorts(split[8]);
                            }
                            containerResources.put(containerName, containerResource);
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 컨테이너를 생성하고 실행하는 함수로 매개변수 imageTag를 기반으로 한다.
     * 이 함수로 실행되는 컨테이너는 `컨테이너 이름, CPU, MEM, GPu, PORT, VOLUME 등` 부가정보가 디폴트이며
     * --rm 옵션이 적용돼 컨테이너 종료 시 삭제가 된다.
     *
     * @param imageTag 도커 이미지 및 태그 정보 (ex. registry:latest)
     * @return 실행 된 컨테이너의 아이디
     * @author leekn
     * @version 1.0.0 20/05/13
     * @see #run(List)
     */
    public DockerCommandStompResponse run(String imageTag) {
        return run(Arrays.asList(DOCKER, RUN, DETACH, RUN_RM, imageTag));
    }

    /**
     * 컨테이너를 생성하고 실행하는 함수로 매개변수 command에 의존적이다.
     * {@link #run(String)}과 다르게 모든 부가정보 및 옵션은 command에 의해 정해지게 된다.
     *
     * @param command 도커의 run 커맨드가 포함 된 커맨드 리스트
     * @return 실행된 컨테이너의 아이디
     * @author leekn
     * @version 1.0.0 20/05/13
     * @see com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand#RUN
     */
    public DockerCommandStompResponse run(List<String> command) {
        String containerId = "0";
        try {
            Process process = new ProcessBuilder(command).start();
            containerId =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    ).readLine();
            if (containerId == null) containerId = "0";
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        DockerCommandStompResponse response = DockerCommandStompResponse.builder().type(Type.RUN).response(Collections.singletonList(containerId)).build();
        System.out.println("agent response = " + response);
        return response;
    }

    public void start(String container) {
        start(Arrays.asList(DOCKER, START, container));
    }

    public void start(List<String> command) {
        noReturnCommand(command);
    }

    public void stop(String container) {
        stop(Arrays.asList(DOCKER, START, container));
    }

    public void stop(List<String> command) {
        noReturnCommand(command);
    }

    public void rm(String container) {
        rm(Arrays.asList(DOCKER, RM, container));
    }

    /**
     * 도커 컨테이너를 삭제한다.
     *
     * @param commands 컨테이너 삭제 명령어 리스트
     * @author leekn
     * @version 1.0.0 20/05/13
     * @see com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand#RM
     */
    public void rm(List<String> commands) {
        System.out.println("##### DockerCommandService.rm");
        System.out.println(commands);
        noReturnCommand(commands);
    }

    /**
     * 저장소에서 이미지를 다운 받는다.
     *
     * @param image 다운받은 이미지 정보이며 레지스트리 정보까지 포함 돼 있다.
     * @return 프로세스 실행 후의 결과값으로 0이면 성공, 그 외는 모두 실패이다.
     */
    public boolean pull(String image) {
        return pull(Arrays.asList(DOCKER, PULL, image));
    }

    /**
     * 저장소에서 이미지를 다운 받는다.
     * commands에 저장된 이미지에는 레지스트리 정보까지 포함 돼 있다.
     *
     * @param command 도커의 PULL 커맨드가 담겨 있는 커맨드 리스트
     * @return 프로세스 실행 후의 결과값으로 0이면 성공, 그 외는 모두 실패이다.
     * @author leekn
     * @version 1.0.0. 20/05/20
     * @see com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand#PULL
     */
    public boolean pull(List<String> command) {
        return returnWithWaitCommand(command) == 0;
    }

    /**
     * 실행 중이거나 끝난 컨테이너 아이디를 반환하는 함수이다.
     * 컨테이너 아이디는 도커 커맨드로 이뤄져 있으며 다음과 같다.
     * docker ps -a --format {{.ID}}
     *
     * @return 도커 컨테이너 아이디 리스트
     */
    private List<String> getContainerIdList() {
        try {
            return new BufferedReader(new InputStreamReader(
                    new ProcessBuilder(DOCKER, PS, PS_ALL, PS_FORMAT_ID).start().getInputStream()
            )).lines().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private void noReturnCommand(List<String> command) {
        try {
            new ProcessBuilder(command).start();
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    private int returnWithWaitCommand(List<String> command) {
        try {
            return new ProcessBuilder(command).start().waitFor();
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return 1;
    }
}
