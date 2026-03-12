package com.taller.app.modules.configuracion.unidadMedida.ui;

import com.taller.app.modules.configuracion.unidadMedida.entity.UnidadMedida;
import com.taller.app.modules.configuracion.unidadMedida.service.UnidadMedidaService;
import com.taller.app.ui.form.BaseFormController;
import com.taller.app.ui.form.FormMode;
import com.taller.app.ui.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnidadMedidaFormController extends BaseFormController {

    private final UnidadMedidaService service;

    private static UnidadMedida unidadSeleccionada;

    public static void setUnidadSeleccionada(UnidadMedida u) {
        unidadSeleccionada = u;
    }

    @FXML private TextField txtNombre;
    @FXML private TextField txtAbreviatura;

    @FXML private Button btnActualizar;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    @FXML
    public void initialize() {

        initFormManager(
                List.of(
                        txtNombre,
                        txtAbreviatura
                ),
                btnActualizar,
                btnGuardar,
                btnCancelar
        );

        if (unidadSeleccionada == null) {
            formStateManager.setMode(FormMode.NEW);
        } else {
            cargarDatos();
            formStateManager.setMode(FormMode.VIEW);
        }
    }

    private void cargarDatos() {
        txtNombre.setText(unidadSeleccionada.getNombre());
        txtAbreviatura.setText(unidadSeleccionada.getAbreviatura());
    }

    @FXML
    public void actualizar() {
        modoEdicion();
    }

    @FXML
    public void cancelar() {

        if (!AlertUtils.confirm("Cancelar cambios", "Se perderán los cambios. ¿Continuar?")) {
            return;
        }

        if (unidadSeleccionada != null) {
            cargarDatos();
            modoLectura();
        } else {
            cerrarModal();
        }
    }

    @FXML
    public void guardar() {

        if (!AlertUtils.confirm("Guardar cambios", "¿Desea guardar la información?")) {
            return;
        }

        try {

            if (unidadSeleccionada == null) {
                unidadSeleccionada = new UnidadMedida();
                unidadSeleccionada.setEstado(1);
            }

            unidadSeleccionada.setNombre(txtNombre.getText());
            unidadSeleccionada.setAbreviatura(txtAbreviatura.getText());

            service.crearOActualizar(unidadSeleccionada);

            AlertUtils.success("Unidad de medida guardada correctamente.");

            unidadSeleccionada = null;
            cerrarModal();

        } catch (Exception e) {
            log.error("Error guardando unidad de medida", e);
            AlertUtils.error(e.getMessage());
        }
    }

    private void cerrarModal() {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }
}

