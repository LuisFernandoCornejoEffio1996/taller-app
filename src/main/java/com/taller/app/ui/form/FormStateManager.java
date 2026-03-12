package com.taller.app.ui.form;

import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.List;

/**
 * Maneja el estado de formularios
 */
public class FormStateManager {

    private final List<Node> fields;

    private final Button btnActualizar;
    private final Button btnGuardar;
    private final Button btnCancelar;

    public FormStateManager(
            List<Node> fields,
            Button btnActualizar,
            Button btnGuardar,
            Button btnCancelar
    ) {
        this.fields = fields;
        this.btnActualizar = btnActualizar;
        this.btnGuardar = btnGuardar;
        this.btnCancelar = btnCancelar;
    }

    public void setMode(FormMode mode) {

        switch (mode) {

            case VIEW -> modoLectura();

            case EDIT, NEW -> modoEdicion();
        }

    }

    private void modoLectura() {

        fields.forEach(f -> f.setDisable(true));

        btnActualizar.setVisible(true);

        btnGuardar.setVisible(false);
        btnCancelar.setVisible(false);
    }

    private void modoEdicion() {

        fields.forEach(f -> f.setDisable(false));

        btnActualizar.setVisible(false);

        btnGuardar.setVisible(true);
        btnCancelar.setVisible(true);
    }

}
