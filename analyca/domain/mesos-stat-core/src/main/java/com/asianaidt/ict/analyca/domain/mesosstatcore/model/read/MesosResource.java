package com.asianaidt.ict.analyca.domain.mesosstatcore.model.read;

import com.asianaidt.ict.analyca.domain.mesosstatcore.model.write.MesosLeader;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MesosResource {
    private String version;
    private String id;
    private String pid;
    private String hostname;
    @JsonProperty("activated_slaves")
    private int activatedSlaves;
    @JsonProperty("deactivated_slaves")
    private int deactivatedSlaves;
    @JsonProperty("unreachable_slaves")
    private int unreachableSlaves;
    private String leader;
    @JsonProperty("log_dir")
    private String logDir;

    @JsonProperty("leader_info")
    private MesosLeader leaderInfo;
    private List<Slave> slaves;
}