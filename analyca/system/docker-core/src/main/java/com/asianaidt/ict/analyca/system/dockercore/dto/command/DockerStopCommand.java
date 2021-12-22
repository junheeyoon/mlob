package com.asianaidt.ict.analyca.system.dockercore.dto.command;

import com.asianaidt.ict.analyca.system.dockercore.dto.DockerCommandStompRequest;
import lombok.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DockerStopCommand implements DockerCommandContainer {
    private String hostName;
    private String containerName;

    /**
     * 웹 상에서 선택한 컨테이너명으로 컨테이너 아이디를 검색하여 설정한다.
     * 도커 커맨드로 변경 시 컨테이너명 대신 컨테이너 아이디를 사용하기 위함이다.
     */
    @Deprecated
    @Setter
    private String containerId;

    @Override
    public DockerCommandStompRequest getCommandStompRequest() {
        List<String> commands = new CopyOnWriteArrayList<>();
        commands.add(DOCKER);
        commands.add(STOP);
        commands.add(containerName);
        return new DockerCommandStompRequest(Type.STOP, commands);
    }
}
