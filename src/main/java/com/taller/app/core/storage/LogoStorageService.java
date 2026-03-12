package com.taller.app.core.storage;

import com.taller.app.core.config.StorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoStorageService {

    private final StorageProperties storageProperties;

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public String guardarLogo(Path origen) {

        try {

            if (!Files.exists(origen)) {
                throw new IllegalArgumentException("Archivo no existe: " + origen);
            }

            Path basePath = Paths.get(storageProperties.getLogos());

            Files.createDirectories(basePath);

            String extension = getExtension(origen.getFileName().toString());

            String nombreArchivo =
                    "logo_" + LocalDateTime.now().format(FORMAT) + extension;

            Path destino = basePath.resolve(nombreArchivo);

            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

            log.info("Logo guardado en {}", destino);

            return nombreArchivo;

        } catch (IOException e) {

            log.error("Error guardando logo", e);

            throw new RuntimeException("No se pudo guardar el logo", e);
        }
    }

    private String getExtension(String filename) {

        int index = filename.lastIndexOf(".");

        if (index == -1) return "";

        return filename.substring(index);
    }

}
