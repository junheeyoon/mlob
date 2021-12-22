package com.asianaidt.ict.analyca.domain.hadoopcore.model.read;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HdfsNameNodeFSSystemResource {
    @JsonProperty("MissingBlocks")
    private int missingBlocks;
    @JsonProperty("CapacityTotal")
    private long capacityTotal;
    @JsonProperty("CapacityUsed")
    private long capacityUsed;
    @JsonProperty("CapacityRemaining")
    private long capacityRemaining;
    @JsonProperty("CapacityUsedNonDFS")
    private long capacityUsedNonDFS;
    @JsonProperty("BlocksTotal")
    private int blocksTotal;
    @JsonProperty("FilesTotal")
    private int totalFiles;
    @JsonProperty("NumLiveDataNodes")
    private int numLiveDataNode;
    @JsonProperty("NumDeadDataNodes")
    private int numDeadDataNode;
    @JsonProperty("NumDecomLiveDataNodes")
    private int numDecomLiveDataNode;
    @JsonProperty("NumDecomDeadDataNodes")
    private int numDecomDeadDataNode;
    @JsonProperty("UnderReplicatedBlocks")
    private long underReplicatedBlock;
    @JsonProperty("PendingDeletionBlocks")
    private long pendingDeletionBlocks;
}
