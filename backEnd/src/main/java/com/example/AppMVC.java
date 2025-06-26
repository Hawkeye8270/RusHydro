package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



//@EnableScheduling         // АВТОСБОРЩИК ДЛЯ БД
@EnableJpaRepositories
@SpringBootApplication
@EntityScan("com.example.entity")
public class AppMVC {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AppMVC.class, args);
    }


}
