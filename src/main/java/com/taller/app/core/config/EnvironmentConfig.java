package com.taller.app.core.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración del ambiente de ejecución.
 */
@Configuration
@Getter
public class EnvironmentConfig {

    @Value("${app.environment:dev}")
    private String environment;

}
