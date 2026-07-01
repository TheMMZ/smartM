package com.smartmaintain.identityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.smartmaintain.identityservice.repositories")
@EntityScan(basePackages = "com.smartmaintain.identityservice.entities")
public class IdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }

}
