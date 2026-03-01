package com.taller.app.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnvironmentValidator {

    private final AppConfig config;

    @PostConstruct
    public void validate() {

        validarRuta(config.getLogPath(), "Logs");
        validarRuta(config.getStorage().getEmpresa().getLogoPath(), "Logos de empresa");
    }

    private void validarRuta(String ruta, String descripcion) {

        if (ruta == null || ruta.isBlank()) {
            throw new IllegalStateException("❌ Falta la propiedad obligatoria: " + descripcion);
        }

        File dir = new File(ruta);

        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            log.info("📁 Creando carpeta {} en {} -> {}", descripcion, ruta, created);
        } else {
            log.info("📁 Carpeta existente para {}: {}", descripcion, ruta);
        }
    }
}
