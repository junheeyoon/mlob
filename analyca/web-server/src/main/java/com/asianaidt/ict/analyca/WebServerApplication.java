package com.asianaidt.ict.analyca;

import com.asianaidt.ict.analyca.domain.containerdomain.service.ContainerStatusService;
import com.asianaidt.ict.analyca.domain.securitycore.repository.MemberRepository;
import com.asianaidt.ict.analyca.domain.securitycore.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.handler.annotation.reactive.MessageMappingMessageHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.MultipartConfigElement;

//@EntityScan("com.asianaidt.ict.analyca.domain")
//@EnableJpaRepositories("com.asianaidt.ict.analyca.domain")
@EnableScheduling
@EnableEurekaServer
@SpringBootApplication
public class WebServerApplication implements CommandLineRunner {
    private static ApplicationContext applicationContext;
    @Autowired
    private RequestMappingHandlerMapping requestHandlerMapping;
    @Autowired
    private ContainerStatusService containerStatusService;

//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private RoleRepository roleRepository;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(WebServerApplication.class, args);
//        displayAllBeans();

    }

//    private static void displayAllBeans() {
//        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
//        for (String beanName : allBeanNames) {
//            System.out.println(beanName);
//        }
//    }

    @Override
    public void run(String... args) throws Exception {
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        String i = "de36f1ea3393";
//        containerStatusService.findContainerStatus(i);
        requestHandlerMapping.getHandlerMethods().forEach((k, v) -> System.out.println("k = " + k + ", v = " + v));
//        Role admin = new Role();
//        admin.setName("ADMIN");
//        roleRepository.save(admin);
//
//        Role user = new Role();
//        user.setName("USER");
//        roleRepository.save(user);
//
//		memberRepository.deleteByUsername("leekn");
//
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		Member member = new Member();
//		member.setUsername("leekn");
//		member.setPassword(passwordEncoder.encode("leekn"));
//
//		Authority authority = new Authority();
//		authority.setRole(admin);
//
//		member.addAuthority(authority);
//		memberRepository.save(member);
    }
}
