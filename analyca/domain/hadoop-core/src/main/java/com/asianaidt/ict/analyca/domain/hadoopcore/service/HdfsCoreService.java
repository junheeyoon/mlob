package com.asianaidt.ict.analyca.domain.hadoopcore.service;

import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsDataNodeResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsNameNodeFSNameInfoResource;
import com.asianaidt.ict.analyca.domain.hadoopcore.model.read.HdfsNameNodeFSSystemResource;

import java.util.List;

public interface HdfsCoreService {
    void write(HdfsNameNodeFSNameInfoResource hdfsNameNodeFSNameInfoResource,
               HdfsNameNodeFSSystemResource hdfsNameNodeFSSystemResource,
               List<HdfsDataNodeResource> listHdfsDataNodeResource);
}
