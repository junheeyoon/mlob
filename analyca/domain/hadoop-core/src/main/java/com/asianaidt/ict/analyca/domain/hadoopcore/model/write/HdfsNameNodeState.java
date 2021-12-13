package com.asianaidt.ict.analyca.domain.hadoopcore.model.write;


import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsNameNodeFSNameInfoResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsNameNodeFSSystemResource;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "namenodestate")
public class HdfsNameNodeState {
    @Column(name = "start_date")
    private String startedTime;
    @Column(name = "version")
    private String version;
    @Column(name = "cluster_id", tag = true)
    private String clusterId;
    @Column(name = "blockpool_id")
    private String blockPoolId;
    @Column(name = "total_files")
    private int totalFiles;
    @Column(name = "block_total")
    private int blocksTotal;
    @Column(name = "capacity_total")
    private long capacityTotal;
    @Column(name = "capacity_used")
    private long capacityUsed;
    @Column(name = "capacity_remaining")
    private long capacityRemaining;
    @Column(name = "capacity_used_non_dfs")
    private long capacityUsedNonDFS;
    @Column(name = "block_pool_used")
    private long blockPoolUsed;
    @Column(name = "block_pool_used_percent")
    private float blockPoolUsedPercent;
    @Column(name = "live_datanode")
    private int numLiveDataNode;
    @Column(name = "dead_datanode")
    private int numDeadDataNode;
    @Column(name = "under_replicated_block")
    private long underReplicatedBlock;
    @Column(name = "pending_deletion_block")
    private long pendingDeletionBlocks;

    public HdfsNameNodeState(final HdfsNameNodeFSSystemResource hdfsNameNodeFSSystemResource,
                             final HdfsNameNodeFSNameInfoResource hdfsNameNodeFSNameInfoResource) {
        this.version = hdfsNameNodeFSNameInfoResource.getVersion();
        this.clusterId = hdfsNameNodeFSNameInfoResource.getClusterId();
        this.blockPoolId = hdfsNameNodeFSNameInfoResource.getBlockPoolId();
        this.totalFiles = hdfsNameNodeFSSystemResource.getTotalFiles();
        this.blocksTotal = hdfsNameNodeFSSystemResource.getBlocksTotal();
        this.capacityTotal = hdfsNameNodeFSSystemResource.getCapacityTotal();
        this.capacityUsed = hdfsNameNodeFSSystemResource.getCapacityUsed();
        this.capacityRemaining = hdfsNameNodeFSSystemResource.getCapacityRemaining();
        this.capacityUsedNonDFS = hdfsNameNodeFSSystemResource.getCapacityUsedNonDFS();
        this.blockPoolUsed = hdfsNameNodeFSNameInfoResource.getBlockPoolUesd();
        this.blockPoolUsedPercent = hdfsNameNodeFSNameInfoResource.getBlockPoolUesdPercent();
        this.numLiveDataNode = hdfsNameNodeFSSystemResource.getNumLiveDataNode();
        this.numDeadDataNode = hdfsNameNodeFSSystemResource.getNumDeadDataNode();
        this.underReplicatedBlock = hdfsNameNodeFSSystemResource.getUnderReplicatedBlock();
        this.pendingDeletionBlocks = hdfsNameNodeFSSystemResource.getPendingDeletionBlocks();
    }
}
