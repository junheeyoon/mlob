package com.asianaidt.ict.analyca.agent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

    @Value("${threadpool.corePoolSize}")
    private Integer corePoolSize;

    @Value("${threadpool.maxPoolSize}")
    private Integer maxPoolSize;

    @Value("${threadpool.queueCapacity}")
    private Integer queueCapacity;

    @Value("${threadpool.threadNamePrefix}")
    private String threadNamePrefix;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() throws IOException {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setBeanName("threadPoolTaskExecutor");
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        taskExecutor.initialize();
        return taskExecutor;
    }
}