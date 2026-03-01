package com.taller.app.modules.configuracion.empresa.controller;

import com.taller.app.modules.configuracion.empresa.model.EmpresaConfig;
import com.taller.app.modules.configuracion.empresa.service.EmpresaConfigService;
import com.taller.app.modules.seguridad.usuario_sistema.model.UsuarioSistema;
import com.taller.app.modules.seguridad.usuario_sistema.service.UsuarioSesionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class EmpresaConfigController {
    private final EmpresaConfigService service;
    private final UsuarioSesionService sesion;

    @FXML private TextField txtRuc;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtNombreComercial;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;

    @FXML private ImageView imgLogo;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnBuscarLogo;
    @FXML private Button btnCancelar;

    private EmpresaConfig actual;
    private String rutaTemporalLogo;


    @FXML
    public void initialize() {
        cargarDatos();
    }

    private void cargarDatos() {

        actual = service.obtenerActiva();

        if (actual != null) {
            txtRuc.setText(actual.getRuc());
            txtRazonSocial.setText(actual.getRazonSocial());
            txtNombreComercial.setText(actual.getNombreComercial());
            txtDireccion.setText(actual.getDireccion());
            txtTelefono.setText(actual.getTelefono());
            txtEmail.setText(actual.getEmail());

            if (actual.getLogoPath() != null) {
                imgLogo.setImage(new Image("file:" + actual.getLogoPath(), false));
            }

            bloquearCampos(true);
            btnGuardar.setVisible(false);
            btnActualizar.setVisible(true);
            btnCancelar.setVisible(false);

        } else {
            bloquearCampos(false);
            btnGuardar.setVisible(true);
            btnActualizar.setVisible(false);
            btnCancelar.setVisible(true);
        }

    }
    private void bloquearCampos(boolean bloquear) {
        txtRuc.setDisable(bloquear);
        txtRazonSocial.setDisable(bloquear);
        txtNombreComercial.setDisable(bloquear);
        txtDireccion.setDisable(bloquear);
        txtTelefono.setDisable(bloquear);
        txtEmail.setDisable(bloquear);
        btnBuscarLogo.setDisable(bloquear);
    }

    @FXML
    private void actualizar() {
        bloquearCampos(false);
        btnGuardar.setVisible(true);
        btnActualizar.setVisible(false);
        btnCancelar.setVisible(true);
    }

    @FXML
    private void buscarLogo() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivo = chooser.showOpenDialog(null);

        if (archivo != null) {
            rutaTemporalLogo = archivo.getAbsolutePath();
            imgLogo.setImage(new Image("file:" + rutaTemporalLogo, false));
        }
    }

    @FXML
    private void guardar() {
        try {
            EmpresaConfig nueva = new EmpresaConfig();

            nueva.setRuc(txtRuc.getText());
            nueva.setRazonSocial(txtRazonSocial.getText());
            nueva.setNombreComercial(txtNombreComercial.getText());
            nueva.setDireccion(txtDireccion.getText());
            nueva.setTelefono(txtTelefono.getText());
            nueva.setEmail(txtEmail.getText());
            nueva.setLogoPath(rutaTemporalLogo);

            service.guardarOActualizar(nueva, sesion.getUsuarioActual());

            mostrarMensaje("Datos guardados correctamente.");
            rutaTemporalLogo = null;
            cargarDatos();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }

    }

    @FXML
    private void cancelar() {
        // Restaurar datos originales desde la BD
        cargarDatos();

        // Limpiar logo temporal
        rutaTemporalLogo = null;

        // Bloquear campos nuevamente
        bloquearCampos(true);

        // Mostrar solo el botón Actualizar
        btnActualizar.setVisible(true);
        btnGuardar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    private void mostrarMensaje(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}


