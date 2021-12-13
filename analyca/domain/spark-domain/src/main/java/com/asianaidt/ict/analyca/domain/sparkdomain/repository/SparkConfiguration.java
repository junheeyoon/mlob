package com.asianaidt.ict.analyca.domain.sparkdomain.repository;



import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application-spark.yml")
public class SparkConfiguration {

//    @Value("${spring.spark.home}")
//    private String sparkHome;
//
//    @Value("${spring.spark.master-uri}")
//    private String masterUri;
//
//    @Value("${spring.spark.appName}")
//    private String appName;

//    @Bean
//    public SparkConf sparkConf() {
//        return new SparkConf()
//                .setAppName(appName)
//                .setSparkHome(sparkHome)
//                .setMaster(masterUri);
//    }
//
//    @Bean
//    public JavaSparkContext javaSparkContext() {
//        return new JavaSparkContext(sparkConf());
//    }
//
//    @Bean
//    public SparkSession sparkSession() {
//        return SparkSession
//                .builder()
//                .sparkContext(javaSparkContext().sc())
//                .appName("Java Spark SQL basic example")
//                .getOrCreate();
//    }
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
}
