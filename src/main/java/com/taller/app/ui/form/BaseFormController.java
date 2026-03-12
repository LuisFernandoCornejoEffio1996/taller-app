package com.taller.app.ui.form;

import com.taller.app.ui.navigation.NavigationService;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.List;

public abstract class BaseFormController {

    protected FormStateManager formStateManager;

    protected NavigationService navigationService;

    protected void initFormManager(
            List<Node> fields,
            Button btnActualizar,
            Button btnGuardar,
            Button btnCancelar
    ) {

        formStateManager = new FormStateManager(
                fields,
                btnActualizar,
                btnGuardar,
                btnCancelar
        );

        formStateManager.setMode(FormMode.VIEW);
    }

    protected void modoLectura() {
        formStateManager.setMode(FormMode.VIEW);
    }

    protected void modoEdicion() {
        formStateManager.setMode(FormMode.EDIT);
    }
}
