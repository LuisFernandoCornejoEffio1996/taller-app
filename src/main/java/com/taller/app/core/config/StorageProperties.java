package com.taller.app.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Rutas configurables del sistema.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "paths")
public class StorageProperties {

    private String logs;
    private String logos;
    private String imagenes;
    private String salida;

}
