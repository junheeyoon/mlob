package com.asianaidt.ict.analyca.domain.schedulercore.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.scheduler")
@EnableJpaRepositories(
        entityManagerFactoryRef = "schedulerEntityManagerFactory",
        transactionManagerRef = "schedulerTransactionManager",
        basePackages = {"com.asianaidt.ict.analyca.domain.schedulercore.repository"})
@EnableTransactionManagement
public class SchedulerDatasourceConfig extends HikariConfig {
    protected Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class);
        props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class);
        props.put("hibernate.hbm2ddl.auto", "update");
//        props.put("hibernate.show_sql", "true");
//        props.put("hibernate.format_sql", "true");
//        props.put("hibernate.use_sql_comments", "true");
        return props;
    }

    @Bean
    public DataSource schedulerDataSource() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(this));
    }

    @Bean
    public EntityManagerFactory schedulerEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(this.schedulerDataSource());
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setPackagesToScan("com.asianaidt.ict.analyca.domain.schedulercore.model");
        factoryBean.setPersistenceUnitName("scheduler");
        factoryBean.setJpaPropertyMap(jpaProperties());
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager schedulerTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(this.schedulerEntityManagerFactory());
        return jpaTransactionManager;
    }
}
