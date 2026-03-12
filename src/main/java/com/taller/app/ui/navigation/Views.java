package com.taller.app.ui.navigation;

/**
 * Enum que define todas las vistas disponibles del sistema.
 * Permite navegación tipada y evita uso de strings.
 */
public enum Views {

    DASHBOARD("/views/home/dashboard.fxml"),
    EMPRESA_CONFIG("/views/empresa_config/empresaConfigView.fxml"),
    UNIDAD_MEDIDA_LIST("/views/unidadmedida/unidadMedidaListView.fxml"),
    UNIDAD_MEDIDA_FORM("/views/unidadmedida/unidadMedidaModalView.fxml");

    private final String path;

    Views(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
