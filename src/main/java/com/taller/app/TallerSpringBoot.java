package com.taller.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.taller.app.modules")
@EntityScan(basePackages = "com.taller.app.modules")
public class TallerSpringBoot {
    public static void main(String[] args) {
        javafx.application.Application.launch(TallerApplication.class, args);
    }
}