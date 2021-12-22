package com.asianaidt.ict.analyca.domain.hadoopcore.model.read;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HdfsNameNodeFSNameInfoResource {
    @JsonProperty("BlockPoolId")
    private String blockPoolId;
    @JsonProperty("ClusterId")
    private String clusterId;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Safemode")
    private boolean safeModeEnabled;
    @JsonProperty("BlockPoolUsedSpace")
    private long blockPoolUesd;
    @JsonProperty("PercentBlockPoolUsed")
    private float blockPoolUesdPercent;

}
