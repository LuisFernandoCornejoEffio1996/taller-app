package com.taller.app.modules.configuracion.unidadMedida.ui;

import com.taller.app.events.UnidadMedidaActualizadaEvent;
import com.taller.app.modules.configuracion.unidadMedida.entity.UnidadMedida;
import com.taller.app.modules.configuracion.unidadMedida.service.UnidadMedidaService;
import com.taller.app.ui.modal.ModalService;
import com.taller.app.ui.navigation.Views;
import com.taller.app.ui.util.AlertUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnidadMedidaListController {

    private final UnidadMedidaService service;
    private final ModalService modalService;

    // Tamaño de página dinámico
    private int pageSize = 2;

    @FXML private TableView<UnidadMedida> tabla;
    @FXML private TableColumn<UnidadMedida, String> colNombre;
    @FXML private TableColumn<UnidadMedida, String> colAbreviatura;
    @FXML private TableColumn<UnidadMedida, Void> colAcciones;

    @FXML private TextField txtBuscar;
    @FXML private Pagination paginacion;
    @FXML private ComboBox<Integer> comboPageSize;
    @FXML private Label lblInfoRegistros;

    // Lista observable con los datos filtrados
    private final ObservableList<UnidadMedida> datosFiltrados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        log.info("Inicializando vista de Unidades de Medida");

        configurarColumnas();
        configurarPageSizeCombo();
        cargarDatos();
        configurarBuscadorTiempoReal();
    }

    /**
     * Configura columnas y botones de acción.
     */
    private void configurarColumnas() {
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colAbreviatura.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAbreviatura()));

        colAcciones.setCellFactory(col -> new TableCell<>() {

            private final Button btnEditar = new Button("Editar");
            private final Button btnDesactivar = new Button("Desactivar");
            private final HBox contenedor = new HBox(10, btnEditar, btnDesactivar);

            {
                btnEditar.getStyleClass().add("btn-warning");
                btnDesactivar.getStyleClass().add("btn-danger");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                UnidadMedida unidad = getTableView().getItems().get(getIndex());

                btnEditar.setOnAction(e -> editar(unidad));
                btnDesactivar.setOnAction(e -> desactivar(unidad));

                setGraphic(contenedor);
            }
        });
    }

    /**
     * Inicializa el combo de tamaño de página.
     */
    private void configurarPageSizeCombo() {
        comboPageSize.getItems().addAll(2, 5, 10, 20, 50);
        comboPageSize.setValue(pageSize);

        comboPageSize.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                log.info("Cambiando tamaño de página a {}", newVal);
                pageSize = newVal;
                configurarPaginacion();
                paginacion.setCurrentPageIndex(0);
            }
        });
    }

    /**
     * Carga datos desde el servicio.
     */
    private void cargarDatos() {
        log.info("Cargando datos de unidades activas");
        datosFiltrados.setAll(service.listarActivos());
        configurarPaginacion();
        paginacion.setCurrentPageIndex(0);
    }

    /**
     * Configura la paginación.
     */
    private void configurarPaginacion() {
        int totalPaginas = (int) Math.ceil((double) datosFiltrados.size() / pageSize);
        paginacion.setPageCount(Math.max(totalPaginas, 1));

        paginacion.setPageFactory(this::crearPagina);

        paginacion.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            actualizarInfoRegistros(newVal.intValue());
        });
    }

    /**
     * Actualiza el mensaje de información de registros.
     */
    private void actualizarInfoRegistros(int pageIndex) {

        int total = datosFiltrados.size();

        if (total == 0) {
            lblInfoRegistros.setText("Mostrando 0 registros");
            return;
        }

        int from = (pageIndex * pageSize) + 1;
        int to = Math.min(from + pageSize - 1, total);

        lblInfoRegistros.setText(
                "Mostrando los registros del " + from + " al " + to +
                        " de un total de " + total + " registros"
        );
    }

    /**
     * Construye la página solicitada.
     */
    private Node crearPagina(int pageIndex) {

        int from = pageIndex * pageSize;
        int to = Math.min(from + pageSize, datosFiltrados.size());

        ObservableList<UnidadMedida> pagina = FXCollections.observableArrayList();

        for (int i = from; i < to; i++) {
            pagina.add(datosFiltrados.get(i));
        }

        tabla.setItems(pagina);
        tabla.refresh();

        // Ajustar altura exacta
        tabla.setFixedCellSize(40);
        tabla.prefHeightProperty().unbind();
        tabla.setPrefHeight((pagina.size() * 40) + 30);

        actualizarInfoRegistros(pageIndex);

        VBox wrapper = new VBox(tabla);
        wrapper.setFillWidth(true);

        return wrapper;
    }

    /**
     * Filtro en tiempo real.
     */
    private void configurarBuscadorTiempoReal() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrar(newValue));
    }

    /**
     * Aplica el filtro.
     */
    private void filtrar(String filtro) {

        String f = filtro.trim().toLowerCase();

        List<UnidadMedida> lista = service.listarActivos().stream()
                .filter(u ->
                        u.getNombre().toLowerCase().contains(f) ||
                                u.getAbreviatura().toLowerCase().contains(f)
                )
                .toList();

        datosFiltrados.setAll(lista);
        configurarPaginacion();
        paginacion.setCurrentPageIndex(0);
    }

    @FXML
    public void nuevo() {
        UnidadMedidaFormController.setUnidadSeleccionada(null);
        modalService.openModal(Views.UNIDAD_MEDIDA_FORM.getPath(), "Nueva Unidad de Medida");
    }

    private void editar(UnidadMedida unidad) {
        UnidadMedidaFormController.setUnidadSeleccionada(unidad);
        modalService.openModal(Views.UNIDAD_MEDIDA_FORM.getPath(), "Editar Unidad de Medida");
    }

    private void desactivar(UnidadMedida unidad) {

        if (!AlertUtils.confirm("Desactivar unidad", "¿Desea desactivar esta unidad?")) {
            return;
        }

        log.info("Desactivando unidad ID={}", unidad.getIdUnidad());
        service.desactivar(unidad.getIdUnidad());

        AlertUtils.success("Unidad desactivada correctamente.");
        cargarDatos();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUnidadActualizada(UnidadMedidaActualizadaEvent event) {
        log.info("Evento AFTER_COMMIT recibido: Unidad actualizada ID={}", event.getIdUnidad());
        cargarDatos();
    }
}


