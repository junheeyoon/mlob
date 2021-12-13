package com.asianaidt.ict.analyca.service.schedulerservice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;


public class QuartzSchedulerConfig {

//    @Autowired
//    DataSource dataSource;

    @Autowired
    private ApplicationContext applicationContext;

//    @Autowired
//    private TriggerListner triggerListner;
//
//    @Autowired
//    private JobsListener jobsListener;

    /*
     * create scheduler
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);                         //////////
//        factory.setDataSource(dataSource);                              ////////// datasource 설정 필요?
        factory.setQuartzProperties(quartzProperties());                ////////// 확인하기

        ////////////////////////////////////////////////////////////////////////// 일단 보류
        //Register listeners to get notification on Trigger misfire etc
//        factory.setGlobalTriggerListeners(triggerListner);
//        factory.setGlobalJobListeners(jobsListener);

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        factory.setJobFactory(jobFactory);

        return factory;
    }

    /**
     * Configure quartz using properties file
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.yml"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

}
