package com.taller.app.config;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EnvironmentValidator {

    private final Environment env;

    public EnvironmentValidator(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void validate() {

        String[] profiles = env.getActiveProfiles();

        if (profiles.length == 0) {
            throw new IllegalStateException("❌ No hay perfiles activos. Configura spring.profiles.active");
        }

        System.out.println("=== Ambiente activo: " + String.join(", ", profiles));

        // Validar propiedad obligatoria
        String logPath = env.getProperty("app.log-path");

        String[] subCarpetas = {"app", "seguridad"};

        if (logPath == null || logPath.isBlank()) {
            throw new IllegalStateException("❌ Falta la propiedad obligatoria: app.log-path");
        }

        for (String folder : subCarpetas){
            File dir = new File(logPath+"/"+folder);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                System.out.println("📁 Creando carpeta de logs: " + logPath + " -> " + created);
            } else {
                System.out.println("📁 Carpeta de logs existente: " + logPath+"/"+folder);
            }
        }
    }
}