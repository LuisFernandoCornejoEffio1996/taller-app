package com.taller.app.core.startup;

import com.taller.app.core.config.StorageProperties;
import com.taller.app.core.logging.LogFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;

/**
 * Inicializa las carpetas del sistema al iniciar la aplicación.
 */
@Component
@RequiredArgsConstructor
public class StartupInitializer {

    private static final Logger log = LogFactory.getLogger(StartupInitializer.class);

    private final StorageProperties storage;

    @PostConstruct
    public void init() {

        createDirectory(storage.getLogs() + "/app");
        createDirectory(storage.getLogs() + "/error");
        createDirectory(storage.getLogs() + "/audit");
        createDirectory(storage.getLogs() + "/startup");
        createDirectory(storage.getLogos());
        createDirectory(storage.getImagenes());
        createDirectory(storage.getSalida());

    }

    private void createDirectory(String path) {

        File dir = new File(path);

        if (!dir.exists()) {

            boolean created = dir.mkdirs();

            if (created) {
                log.info("Directorio creado: {}", path);
            } else {
                log.warn("No se pudo crear el directorio: {}", path);
            }

        }

    }

}