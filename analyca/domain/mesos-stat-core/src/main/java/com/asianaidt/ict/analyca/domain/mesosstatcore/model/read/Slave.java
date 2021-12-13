package com.asianaidt.ict.analyca.domain.mesosstatcore.model.read;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Slave {
    private String id;
    private String hostname;
    private int port;
    private String pid;
    private SlaveResource resources;
    @JsonProperty("used_resources")
    private SlaveResource usedRresources;
    @JsonProperty("offered_resources")
    private SlaveResource offeredResources;
    @JsonProperty("reserved_resources")
    private SlaveResource reservedResources;
    @JsonProperty("unreserved_resources")
    private SlaveResource unreservedResources;
    private boolean active;
    private String version;
}
