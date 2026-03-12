package com.taller.app.modules.configuracion.empresa.ui;

import com.taller.app.core.config.StorageProperties;
import com.taller.app.core.storage.LogoStorageService;
import com.taller.app.modules.configuracion.empresa.entity.EmpresaConfig;
import com.taller.app.modules.configuracion.empresa.service.EmpresaConfigService;
import com.taller.app.ui.form.BaseFormController;
import com.taller.app.ui.navigation.NavigationService;
import com.taller.app.ui.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmpresaConfigController extends BaseFormController {

    private final EmpresaConfigService empresaService;
    private final LogoStorageService logoStorageService;
    private final StorageProperties storageProperties;

    private EmpresaConfig empresaActual;

    private String rutaLogoSeleccionado;

    @FXML private TextField txtRuc;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtNombreComercial;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;

    @FXML private ImageView imgLogo;

    @FXML private Button btnActualizar;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnBuscarLogo;

    @FXML
    public void initialize() {

        initFormManager(
                List.of(
                        txtRuc,
                        txtRazonSocial,
                        txtNombreComercial,
                        txtDireccion,
                        txtTelefono,
                        txtEmail
                ),
                btnActualizar,
                btnGuardar,
                btnCancelar
        );

        btnBuscarLogo.setDisable(true);

        cargarEmpresa();
    }

    private void cargarEmpresa() {

        try {

            log.info("Cargando configuración de empresa");

            empresaActual = empresaService.obtenerEmpresa();

            if (empresaActual == null) {

                log.warn("No existe configuración de empresa registrada");

                empresaActual = new EmpresaConfig();
                return;
            }

            txtRuc.setText(empresaActual.getRuc());
            txtRazonSocial.setText(empresaActual.getRazonSocial());
            txtNombreComercial.setText(empresaActual.getNombreComercial());
            txtDireccion.setText(empresaActual.getDireccion());
            txtTelefono.setText(empresaActual.getTelefono());
            txtEmail.setText(empresaActual.getEmail());

            cargarLogo();

            log.info("Configuración de empresa cargada correctamente");

        } catch (Exception e) {

            log.error("Error cargando configuración de empresa", e);

            AlertUtils.error("No se pudo cargar la configuración de la empresa");
        }
    }

    private void cargarLogo() {

        try {

            if (empresaActual.getLogoPath() == null) {
                return;
            }

            Path logoPath = Paths.get(
                    storageProperties.getLogos(),
                    empresaActual.getLogoPath()
            );

            if (logoPath.toFile().exists()) {

                imgLogo.setImage(
                        new Image(logoPath.toUri().toString())
                );

                log.info("Logo cargado {}", logoPath);

            } else {

                log.warn("Logo no encontrado {}", logoPath);
            }

        } catch (Exception e) {

            log.error("Error cargando logo", e);
        }
    }

    @FXML
    public void actualizar() {

        log.info("Activando modo edición empresa");

        modoEdicion();

        btnBuscarLogo.setDisable(false);
    }

    @FXML
    public void cancelar() {

        boolean confirmar = AlertUtils.confirm(
                "Cancelar cambios",
                "Se perderán los cambios realizados. ¿Desea continuar?"
        );

        if (!confirmar) {
            return;
        }

        log.info("Edición cancelada");

        cargarEmpresa();

        modoLectura();

        btnBuscarLogo.setDisable(true);
    }

    @FXML
    public void guardar() {

        boolean confirmar = AlertUtils.confirm(
                "Guardar cambios",
                "¿Desea guardar las modificaciones realizadas?"
        );

        if (!confirmar) {
            return;
        }

        try {

            empresaActual.setRuc(txtRuc.getText());
            empresaActual.setRazonSocial(txtRazonSocial.getText());
            empresaActual.setNombreComercial(txtNombreComercial.getText());
            empresaActual.setDireccion(txtDireccion.getText());
            empresaActual.setTelefono(txtTelefono.getText());
            empresaActual.setEmail(txtEmail.getText());

            guardarLogoSiExiste();

            empresaActual = empresaService.guardar(empresaActual);

            log.info(
                    "Empresa actualizada correctamente id={}",
                    empresaActual.getIdEmpresaConfig()
            );

            AlertUtils.success("Datos de empresa actualizados correctamente");

            modoLectura();

            btnBuscarLogo.setDisable(true);

        } catch (Exception e) {

            log.error("Error guardando configuración de empresa", e);

            AlertUtils.error("No se pudo guardar la configuración de la empresa");
        }
    }

    private void guardarLogoSiExiste() {

        if (rutaLogoSeleccionado == null) {
            return;
        }

        try {

            Path origen = Path.of(rutaLogoSeleccionado);

            String nombreLogo = logoStorageService.guardarLogo(origen);

            empresaActual.setLogoPath(nombreLogo);

            log.info("Logo guardado {}", nombreLogo);

        } catch (Exception e) {

            log.error("Error guardando logo", e);

            AlertUtils.error("No se pudo guardar el logo");
        }
    }

    @FXML
    public void buscarLogo() {

        try {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Seleccionar logo de empresa");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(
                            "Imágenes",
                            "*.png",
                            "*.jpg",
                            "*.jpeg"
                    )
            );

            File file = fileChooser.showOpenDialog(null);

            if (file == null) {
                return;
            }

            rutaLogoSeleccionado = file.getAbsolutePath();

            imgLogo.setImage(
                    new Image(file.toURI().toString())
            );

            log.info("Logo seleccionado {}", rutaLogoSeleccionado);

        } catch (Exception e) {

            log.error("Error seleccionando logo", e);

            AlertUtils.error("No se pudo cargar el logo seleccionado");
        }
    }

}
