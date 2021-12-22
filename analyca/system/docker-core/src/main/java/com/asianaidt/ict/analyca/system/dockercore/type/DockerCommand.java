package com.asianaidt.ict.analyca.system.dockercore.type;

public final class DockerCommand {
    public static final String REGISTRY_HOST = "10.33.194.14:4506";
    public static final String DOCKER = "docker";
    public static final String PS = "ps";
    public static final String PS_ALL = "-a";
    public static final String PS_FORMAT = "--format table {{.ID}}|{{.Image}}|{{.Ports}}|{{.Status}}|{{.Names}}";
    public static final String PS_FORMAT_ID = "--format={{.ID}}";
    public static final String INSPECT = "inspect";
    public static final String INSPECT_FORMAT = "--format={{.Name}}|" +
            "{{.Id}}|" +
            "{{.HostConfig.NanoCpus}}|" +
            "{{.HostConfig.Memory}}|" +
            "{{.State.StartedAt}}|" +
            "{{.State.Status}}|" +
            "{{.Config.Image}}|" +
//        "{{.Config.Labels.Creator}}|" +
            "{{.HostConfig.Binds}}|" +
            "{{range $p, $conf := .HostConfig.PortBindings}} {{$p}}:{{(index $conf 0).HostPort}} {{end}}";
    public static final String STATS = "stats";
    public static final String STATS_NOSTREAM = "--no-stream";
    public static final String STATS_FORMAT = "--format={{.Name}}|{{.ID}}|{{.CPUPerc}}|{{.MemUsage}}|{{.NetIO}}|{{.BlockIO}}";
    public static final String RUN = "run";
    public static final String RUN_RM = "--rm";
    public static final String DETACH = "-d";
    public static final String CONTAINER_NAME = "--name";
    public static final String PORT = "-p";
    public static final String VOLUME = "-v";
    public static final String LABELS = "-l";
    public static final String RM = "rm";
    public static final String PULL = "pull";
    public static final String START = "start";
    public static final String STOP = "stop";
    public static final String MEMORY = "-m";
    public static final String CPU = "--cpus";
    public static final String GPU = "--gpus";

    public static final String FORMAT_DELIMITER = "\\|";

    public static String image(String name, String tag) {
        return REGISTRY_HOST + "/" + name + ":" + tag;
    }

    public static String creator(String name) {
        return "Creator=" + name;
    }

    public enum Type {
        RUN, START, STOP, RM
    }

    public static class WebSocket {
        private static final String BaseUri = "/analyca/ghost";
        private static final String SendToUser = "/user";

        public enum ServerToAgent {
            COMMAND("/ghost/command");

            private String uri;

            ServerToAgent(String uri) {
                this.uri = uri;
            }

            public String getUri() {
                return uri;
            }

            public String getUriForUser() {
                return SendToUser + uri;
            }
        }

        public enum AgentToServer {
            METRICS("/collect"),
            COMMAND("/command");

            private String uri;

            AgentToServer(String uri) {
                this.uri = uri;
            }

            public String getUri() {
                return BaseUri + uri;
            }
        }
    }
}