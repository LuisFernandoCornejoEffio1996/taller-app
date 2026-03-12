package com.taller.app.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración general del sistema.
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class AppConfig {
}
