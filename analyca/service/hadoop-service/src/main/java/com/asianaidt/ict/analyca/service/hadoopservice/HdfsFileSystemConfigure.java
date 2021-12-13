package com.asianaidt.ict.analyca.service.hadoopservice;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spring.hadoop")
public class HdfsFileSystemConfigure {

    private String uri;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }
}
