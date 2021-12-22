package com.asianaidt.ict.analyca.domain.hadoopcore.model.read;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HdfsDataNodeResource {
    private String name;
    private String hostname;
    @JsonProperty("capacity")
    private long hdfsCapacity;
    @JsonProperty("usedSpace")
    private long hdfsUsed;
    @JsonProperty("nonDfsUsedSpace")
    private long hdfsNonUsed;
    @JsonProperty("remaining")
    private long hdfsRemaining;
    private float hdfsUsedPercent;
    private float hdfsRemainingPercent;
    private long cacheCapacity;
    private long cacheUsed;
    private long cacheRemaining;
    private float cacheUsedPercent;
    private float cacheRemainingPercent;
    private int xceiverCount;
    private String lastUpdate;
    private String lastBlockReportTime;
    @JsonProperty("numBlocks")
    private int numBlocks;
    private int lastContact;

}
