package com.taller.app.ui;

import com.taller.app.TallerApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainController implements Initializable {
    @FXML private StackPane contenedorPrincipal;

    private static MainController instance;

    public MainController() {
        instance = this;
    }

    public static MainController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        abrirInicio(); // ← carga InicioView.fxml automáticamente
    }

    private void cargarVista(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(TallerApplication.class.getResource(ruta));
            loader.setControllerFactory(TallerApplication.getContext()::getBean);

            Node vista = loader.load();
            contenedorPrincipal.getChildren().setAll(vista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirEmpresaConfig() {
        TallerApplication.setTitle("Configuración de Empresa");
        cargarVista("/views/configuracion/empresa/EmpresaConfigView.fxml");
    }

    @FXML
    public void abrirUsuarios() {
        TallerApplication.setTitle("Usuarios");
        cargarVista("/views/seguridad/usuarios/UsuarioView.fxml");
    }

    @FXML
    public void abrirInicio() {
        TallerApplication.setTitle("Inicio");
        // Cargar la vista principal o dashboard
        cargarVista("/views/home/InicioView.fxml");
    }
}

