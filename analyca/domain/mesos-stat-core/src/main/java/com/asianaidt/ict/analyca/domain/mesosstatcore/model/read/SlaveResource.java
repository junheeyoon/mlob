package com.asianaidt.ict.analyca.domain.mesosstatcore.model.read;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlaveResource {
    private int disk;
    private int mem;
    private int gpus;
    private int cpus;
    private String ports;
}
