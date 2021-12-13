package com.asianaidt.ict.analyca.system.dockercore.dto.command;

import com.asianaidt.ict.analyca.system.dockercore.dto.DockerCommandStompRequest;
import com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand;
import lombok.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DockerRMCommand implements DockerCommandContainer {
    private String hostName;
    /**
     * 삭제 대상인 컨테이너의 이름
     *
     * @author leekn
     * @version 1.0.0 20/05/13
     * @see #getCommandStompRequest()
     * @since 1.0.0
     */
    @Builder.Default
    private String containerName = "";

    /**
     * 웹 상에서 선택한 컨테이너명으로 컨테이너 아이디를 검색하여 설정한다.
     * 도커 커맨드로 변경 시 컨테이너명 대신 컨테이너 아이디를 사용하기 위함이다.
     *
     * @author leekn
     * @version 1.0.0 20/05/13
     * @since 1.0.0
     * @deprecated
     */
    @Builder.Default
    @Deprecated
    @Setter
    private String containerId = "";

    @Override
    public DockerCommandStompRequest getCommandStompRequest() {
        List<String> commands = new CopyOnWriteArrayList<>();
        commands.add(DOCKER);
        commands.add(RM);
        commands.add(containerName.equals("") ? containerId : containerName);
        return new DockerCommandStompRequest(DockerCommand.Type.RM, commands);
    }
}