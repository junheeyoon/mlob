package com.asianaidt.ict.observability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EntityScan("com.asianaidt.ict.analyca.domain")
//@EnableJpaRepositories("com.asianaidt.ict.analyca.domain")
@SpringBootApplication
public class WebServerApplication implements CommandLineRunner {
    private static ApplicationContext applicationContext;
    @Autowired
    private RequestMappingHandlerMapping requestHandlerMapping;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(WebServerApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        requestHandlerMapping.getHandlerMethods().forEach((k, v) -> System.out.println("k = " + k + ", v = " + v));
    }
}
