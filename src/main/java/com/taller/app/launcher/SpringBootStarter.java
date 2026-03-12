package com.taller.app.launcher;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Punto de arranque del contexto Spring.
 */
@SpringBootApplication(scanBasePackages = "com.taller.app")
@EnableJpaRepositories(basePackages = "com.taller.app")
@EntityScan(basePackages = "com.taller.app")
public class SpringBootStarter {
}
