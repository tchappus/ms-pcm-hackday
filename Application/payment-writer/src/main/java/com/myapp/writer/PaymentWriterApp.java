package com.myapp.writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;

@SpringBootApplication
@EnableReactiveCosmosRepositories("com.myapp.lib.repo")
public class PaymentWriterApp {
    
    public static void main(String[] args) {
        SpringApplication.run(PaymentWriterApp.class, args);
    }
}
