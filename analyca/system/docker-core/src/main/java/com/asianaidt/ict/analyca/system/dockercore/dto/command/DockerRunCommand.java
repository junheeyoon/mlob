package com.asianaidt.ict.analyca.system.dockercore.dto.command;

import com.asianaidt.ict.analyca.system.dockercore.dto.DockerCommandStompRequest;
import com.asianaidt.ict.analyca.system.dockercore.util.DockerOptionParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.asianaidt.ict.analyca.system.dockercore.type.DockerCommand.*;

@Getter
@ToString
@NoArgsConstructor
public class DockerRunCommand implements DockerCommandContainer {
    @NonNull
    private String hostName;
    @NonNull
    private String imageName;
    private String tag = "latest";
    private String containerName = "";
    private String port = "";
    private String volume = "";
    private String creator = "";
    private String memory = "";
    private String cpu = "";
    private Boolean gpu = false;
    private String options = "";
    private String commands = "";
    // 응답 유무
    private boolean useResponse = false;
    private Long idLogStep = 0L;
    private boolean useExtraCommands = false;

    public DockerRunCommand(@NonNull String hostName, @NonNull String imageName,
                            String tag, String containerName,
                            String port, String volume,
                            String creator, String memory,
                            String cpu, Boolean gpu) {
        this.hostName = hostName;
        this.imageName = imageName;
        this.tag = tag;
        this.containerName = containerName;
        this.port = port;
        this.volume = volume;
        this.creator = creator;
        this.memory = memory;
        this.cpu = cpu;
        this.gpu = gpu;
    }

    public DockerRunCommand(@NonNull String hostName, @NonNull String imageName,
                            String tag, String containerName,
                            String port, String volume,
                            String creator, String memory,
                            String cpu, Boolean gpu,
                            String options, String commands,
                            boolean useResponse,
                            Long idLogStep
    ) {
        this.hostName = hostName;
        this.imageName = imageName;
        this.tag = tag;
        this.containerName = containerName;
        this.port = port;
        this.volume = volume;
        this.creator = creator;
        this.memory = memory;
        this.cpu = cpu;
        this.gpu = gpu;
        this.options = options;
        this.commands = commands;
        this.useResponse = useResponse;
        this.idLogStep = idLogStep;
    }

    @Override
    public DockerCommandStompRequest getCommandStompRequest() {
        List<String> commands = new CopyOnWriteArrayList<>();
        commands.add(DOCKER);
        commands.add(RUN);
        commands.add(DETACH);
        if (!containerName.equals("")) {
            commands.add(CONTAINER_NAME);
            commands.add(containerName);
        }
        if (!port.equals("")) {
            Arrays.stream(port.split("\\s+")).forEach(p -> {
                commands.add(PORT);
                commands.add(p);
            });
        }
        if (!volume.equals("")) {
            commands.add(VOLUME);
            commands.add(volume);
        }
        if (!creator.equals("")) {
            commands.add(LABELS);
            commands.add(creator(creator));
        }
        if (!memory.equals("")) {
            commands.add(MEMORY);
            commands.add(memory);
        }
        if (!cpu.equals("")) {
            commands.add(CPU);
            commands.add(cpu);
        }
        if (gpu) {
            commands.add(GPU);
        }
        if (!options.equals("")) {
            commands.addAll(DockerOptionParser.parsing(options));
        }

        commands.add(image(imageName, tag));

        if(!this.commands.equals("")) {
            commands.add(this.commands);
            useExtraCommands = true;
        }

        return DockerCommandStompRequest.builder(Type.RUN, commands)
                .useResponse(useResponse)
                .idLogStep(idLogStep)
                .useExtraCommands(useExtraCommands)
                .build();
    }
}