package com.asianaidt.ict.analyca.agent.ghost;

import com.asianaidt.ict.analyca.agent.stomp.StompClient;
import com.asianaidt.ict.analyca.service.dockerservice.DockerCommandService;
import com.asianaidt.ict.analyca.system.dockercore.dto.DockerCommandStompRequest;
import com.asianaidt.ict.analyca.system.dockercore.dto.DockerCommandStompResponse;
import com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.WebSocket.AgentToServer.COMMAND;

@Slf4j
@Service
public class GhostService {
    private final StompClient stompClient;
    private final GhostCollectorScheduler ghostCollectorScheduler;
    private final DockerCommandService dockerCommandService;

    @Lazy
    public GhostService(StompClient stompClient,
                        GhostCollectorScheduler ghostCollectorScheduler,
                        DockerCommandService dockerCommandService) {
        this.stompClient = stompClient;
        this.ghostCollectorScheduler = ghostCollectorScheduler;
        this.dockerCommandService = dockerCommandService;
    }

    /**
     * 도커 명령어를 구분하여 실행하는 함수이다.
     *
     * @param request
     * @author leekn
     * @version 20/05/20
     * @see com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.Type
     * @see com.asianaidt.ict.analyca.service.dockerservice.DockerCommandService
     */
    public void execute(DockerCommandStompRequest request) {
        log.debug("request = " + request);
        try {
            switch (request.getType()) {
                case RUN:
                    // 직접 실행했을 때와 스케쥴러에 의해 실행 됐을 때의 구분이 필요하지 않을까
                    final String image =
                            request.isUseExtraCommands() ?
                            request.getCommands().get(request.getCommands().size() - 2) :
                            request.getCommands().get(request.getCommands().size() - 1);
                    if (dockerCommandService.pull(image)) {
                        DockerCommandStompResponse response = dockerCommandService.run(request.getCommands());
                        if (request.isUseResponse()) {
                            response.setIdLogStep(request.getIdLogStep());
                            stompClient.send(COMMAND.getUri(),response);
                        }
                    }
//                    DockerCommandStompResponse demo = new DockerCommandStompResponse();
//                    demo.setIdLogStep(request.getIdLogStep());
//                    demo.setType(DockerCommand.Type.RUN);
//                    demo.setResponse(Collections.singletonList("aaaaaaaaaaaaaaaaa"));
//                    stompClient.send(COMMAND.getUri(),demo);
                    break;
                case START:
                    dockerCommandService.start(request.getCommands());
                    break;
                case STOP:
                    dockerCommandService.stop(request.getCommands());
                    break;
                case RM:
                    dockerCommandService.rm(request.getCommands());
                    break;
                default:
                    log.error("잘못 된 도커 명령어를 실행하였습니다.");
            }
            ghostCollectorScheduler.collect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}