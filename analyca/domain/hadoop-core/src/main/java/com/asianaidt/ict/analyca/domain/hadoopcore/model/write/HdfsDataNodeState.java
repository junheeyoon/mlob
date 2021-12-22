package com.asianaidt.ict.analyca.domain.hadoopcore.model.write;


import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsDataNodeResource;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@Measurement(name = "datanodestate")
public class HdfsDataNodeState {
    @Column(name = "hostname", tag = true)
    private String hostname;
    @Column(name = "hdfs_capacity")
    private long hdfsCapacity;
    @Column(name = "hdfs_used")
    private long hdfsUsed;
    @Column(name = "hdfs_non_used")
    private long hdfsNonUsed;
    @Column(name = "hdfs_remaining")
    private long hdfsRemaining;
    //    @Column(name = "cache_capacity")
//    private long cacheCapacity;
//    @Column(name = "cache_used")
//    private long cacheUsed;
//    @Column(name = "cache_remaining")
//    private long cacheRemaining;
//    @Column(name = "xceiver_count")
//    private int xceiverCount;
    @Column(name = "last_contact")
    private int lastContact;
    //    @Column(name = "last_block_report_time")
//    private String lastBlockReportTime;
    @Column(name = "num_blocks")
    private int numBlocks;

    public HdfsDataNodeState(HdfsDataNodeResource hdfsDataNodeResource) {
        this.hostname = hdfsDataNodeResource.getHostname();
        this.hdfsCapacity = hdfsDataNodeResource.getHdfsCapacity();
        this.hdfsUsed = hdfsDataNodeResource.getHdfsUsed();
        this.hdfsNonUsed = hdfsDataNodeResource.getHdfsNonUsed();
        this.hdfsRemaining = hdfsDataNodeResource.getHdfsRemaining();
//        this.cacheCapacity = hdfsDataNodeResource.getCacheCapacity();
//        this.cacheUsed = hdfsDataNodeResource.getCacheUsed();
//        this.cacheRemaining = hdfsDataNodeResource.getCacheRemaining();
//        this.xceiverCount = hdfsDataNodeResource.getXceiverCount();
        this.lastContact = hdfsDataNodeResource.getLastContact();
//        this.lastBlockReportTime = hdfsDataNodeResource.getLastBlockReportTime();
        this.numBlocks = hdfsDataNodeResource.getNumBlocks();
    }
}
