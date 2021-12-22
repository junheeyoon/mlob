package com.asianaidt.ict.analyca.domain.hadoopcore.model.read;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HdfsNameNodeJvmResouce {
    @JsonProperty("MemHeapUsedM")
    private float memHeapUsedM;
    @JsonProperty("MemHeapCommittedM")
    private float memHeapCommittedM;
    @JsonProperty("MemHeapMaxM")
    private float memHeapMaxM;
    @JsonProperty("MemNonHeapUsedM")
    private float memNonHeapUsedM;
    @JsonProperty("MemNonHeapMaxM")
    private float memNonHeapMaxM;
    @JsonProperty("MemNonHeapCommittedM")
    private float memNonHeapCommittedM;
}
