package com.taller.app.ui.navigation;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

/**
 * Loader centralizado para vistas FXML integradas con spring
 */
@Component
@Slf4j
public class ViewLoader {

    private final ApplicationContext context;

    public ViewLoader(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Carga una vista FXML utilizando Spring como factory de controllers.
     *
     * @param fxmlPath ruta del archivo FXML dentro de resources
     * @return Parent nodo raíz de la vista
     */
    public Parent load(String fxmlPath) {

        try {

            URL resourse = getClass().getResource(fxmlPath);

            if (resourse == null){

                log.error("FXML no encontrado: {}", fxmlPath);

                throw new IllegalStateException("FXML no encontrado en classpath: "+fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(resourse);

            loader.setControllerFactory(context::getBean);

            return loader.load();

        } catch (IOException e) {

            log.error("Error cargando vista {}", fxmlPath, e);

            throw new RuntimeException("No se pudo cargar la vista: " + fxmlPath, e);

        }

    }

    public Parent loadForModal(String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);

            if (resource == null) {
                log.error("FXML no encontrado: {}", fxmlPath);
                throw new IllegalStateException("FXML no encontrado: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(resource);
            loader.setControllerFactory(context::getBean);

            return loader.load();

        } catch (IOException e) {
            log.error("Error cargando vista modal {}", fxmlPath, e);
            throw new RuntimeException("No se pudo cargar la vista modal: " + fxmlPath, e);
        }
    }
}
