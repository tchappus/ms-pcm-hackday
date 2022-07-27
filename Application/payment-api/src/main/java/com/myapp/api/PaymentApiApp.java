package com.myapp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;

@SpringBootApplication
@EnableReactiveCosmosRepositories("com.myapp.lib.repo")
public class PaymentApiApp {
    
    public static void main(String[] args) {
        SpringApplication.run(PaymentApiApp.class, args);
    }
}
