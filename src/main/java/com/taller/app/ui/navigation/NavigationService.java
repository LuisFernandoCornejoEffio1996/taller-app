package com.taller.app.ui.navigation;

import com.taller.app.events.VistaCambiadaEvent;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Servicio de navegación central del sistema.
 * Controla qué vista se muestra en el contenedor principal.
 */
@Service
@Slf4j
public class NavigationService {

    private final ViewLoader viewLoader;
    private final ApplicationEventPublisher publisher;

    private StackPane mainContainer;

    public NavigationService(ViewLoader viewLoader, ApplicationEventPublisher publisher) {
        this.viewLoader = viewLoader;
        this.publisher = publisher;
    }

    public void setMainContainer(StackPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    /**
     * Navega hacia una vista definida en el enum.
     */
    public void navigateTo(Views view) {

        if (mainContainer == null) {
            throw new IllegalStateException("Main container no inicializado");
        }

        Parent root = viewLoader.load(view.getPath());

        // 🔥 Leer título desde el FXML
        String tituloModulo = (String) root.getUserData();

        // 🔥 Notificar al MainLayoutController
        publisher.publishEvent(new VistaCambiadaEvent(this, tituloModulo));

        mainContainer.getChildren().setAll(root);

        log.info("Navegando a {}", view.name());
    }

}
