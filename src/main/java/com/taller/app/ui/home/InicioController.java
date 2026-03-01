package com.taller.app.ui.home;

import com.taller.app.ui.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

@Component
public class InicioController {
    @FXML
    private Button btnEmpresa;

    @FXML
    private Button btnUsuarios;

    @FXML
    public void initialize() {
        btnEmpresa.setOnAction(e -> MainController.getInstance().abrirEmpresaConfig());
        btnUsuarios.setOnAction(e -> MainController.getInstance().abrirUsuarios());
    }

}
