package com.asianaidt.ict.analyca.system.dockercore.dto;

import com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "hiddenBuilder")
public class DockerCommandStompRequest {
    private DockerCommand.Type type;
    private List<String> commands;
    // Type에 대응하는 응답을 사용할지 판단하는 변수.
    @Builder.Default private boolean useResponse = false;
    @Builder.Default private Long idLogStep = 0L;
    @Builder.Default private boolean useExtraCommands = false;

    public static DockerCommandStompRequestBuilder builder(DockerCommand.Type type, List<String> commands) {
        return hiddenBuilder().type(type).commands(commands);
    }

    public DockerCommandStompRequest(DockerCommand.Type type, List<String> commands) {
        this.type = type;
        this.commands = commands;
    }

    public DockerCommandStompRequest(DockerCommand.Type type, List<String> commands, boolean useResponse, Long idLogStep) {
        this.type = type;
        this.commands = commands;
        this.useResponse = useResponse;
        this.idLogStep = idLogStep;
    }
}