package com.taller.app.ui.main;

import com.taller.app.events.EmpresaActualizadaEvent;
import com.taller.app.events.VistaCambiadaEvent;
import com.taller.app.modules.configuracion.empresa.service.EmpresaConfigService;
import com.taller.app.ui.navigation.NavigationService;
import com.taller.app.ui.navigation.Views;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MainLayoutController {

    private final NavigationService navigationService;
    private final EmpresaConfigService empresaConfigService;
    private final StageManager stageManager;

    @FXML private StackPane mainContent;
    @FXML private VBox sidebar;
    @FXML private Label lblTituloSistema;

    // Botones principales
    @FXML private Button btnInicio;
    @FXML private Button btnConfigHeader;
    @FXML private Button btnEmpresaConfig;
    @FXML private Button btnUnidadMedida;

    // Submenú
    @FXML private VBox submenuConfig;

    private boolean submenuAbierto = false;
    private Button activeButton;

    // Iconos
    @FXML private FontIcon iconHome;
    @FXML private FontIcon icomBuilding;
    @FXML private FontIcon iconBalanceScale;
    @FXML private FontIcon iconConfig;
    @FXML private FontIcon iconArrowConfig;

    @FXML
    public void initialize() {

        navigationService.setMainContainer(mainContent);

        submenuConfig.setVisible(false);
        submenuConfig.setManaged(false);
        iconArrowConfig.setRotate(0);

        iniciarIconos();
        inicializarTooltips();

        // Obtener nombre de empresa desde BD
        String nombreEmpresa = empresaConfigService.obtenerEmpresa().getNombreComercial();
        lblTituloSistema.setText(nombreEmpresa);

        // Asegurar que el menú inicie expandido
        sidebar.setPrefWidth(260);

        // Vista inicial
        navigationService.navigateTo(Views.DASHBOARD);
        setActive(btnInicio);

        log.info("Aplicación iniciada, cargando dashboard");

    }

    // ---------------------------------------------------------
    // ICONOS
    // ---------------------------------------------------------
    private void iniciarIconos() {
        iconHome.setIconCode(FontAwesomeSolid.HOME);
        icomBuilding.setIconCode(FontAwesomeSolid.BUILDING);
        iconBalanceScale.setIconCode(FontAwesomeSolid.BALANCE_SCALE);
        iconConfig.setIconCode(FontAwesomeSolid.COG);
    }

    // ---------------------------------------------------------
    // TOOLTIP SOLO CUANDO ESTÁ COLAPSADO
    // ---------------------------------------------------------
    private void inicializarTooltips() {
        btnInicio.setTooltip(new Tooltip("Inicio"));
        btnConfigHeader.setTooltip(new Tooltip("Configuración"));
        btnEmpresaConfig.setTooltip(new Tooltip("Empresa"));
        btnUnidadMedida.setTooltip(new Tooltip("Unidades de Medida"));
    }

    // ---------------------------------------------------------
    // SUBMENÚ CONFIGURACIÓN
    // ---------------------------------------------------------
    @FXML
    public void toggleConfig() {

        submenuAbierto = !submenuAbierto;

        submenuConfig.setVisible(submenuAbierto);
        submenuConfig.setManaged(submenuAbierto);

        // Animación de rotación
        RotateTransition rt = new RotateTransition(Duration.millis(200), iconArrowConfig);
        rt.setToAngle(submenuAbierto ? 180 : 0);
        rt.play();

        if (submenuAbierto) {
            iconArrowConfig.getStyleClass().add("submenu-open");
        } else {
            iconArrowConfig.getStyleClass().remove("submenu-open");
        }

        log.debug("Submenú configuración {}", submenuAbierto ? "abierto" : "cerrado");
    }

    // ---------------------------------------------------------
    // NAVEGACIÓN
    // ---------------------------------------------------------
    @FXML
    public void abrirDashboard() {
        navigationService.navigateTo(Views.DASHBOARD);
        setActive(btnInicio);
        log.debug("Navegando a dashboard");
    }

    @FXML
    public void abrirEmpresaConfig() {
        navigationService.navigateTo(Views.EMPRESA_CONFIG);
        setActive(btnEmpresaConfig);
        log.debug("Navegando a configuración de empresa");
    }

    @FXML
    public void abrirUnidadMedida() {
        navigationService.navigateTo(Views.UNIDAD_MEDIDA_LIST);
        setActive(btnUnidadMedida);
        log.info("Navegando a unidad de medida");
    }

    // ---------------------------------------------------------
    // ESTADO ACTIVO
    // ---------------------------------------------------------
    public void setActive(Button btn) {

        if (activeButton != null) {
            activeButton.getStyleClass().remove("sidebar-active");
        }

        btn.getStyleClass().add("sidebar-active");
        activeButton = btn;
    }


    @EventListener
    public void onVistaCambiada(VistaCambiadaEvent event) {

        Platform.runLater(() -> {

            if (mainContent.getScene() == null) {
                return;
            }

            Stage stage = (Stage) mainContent.getScene().getWindow();

            String nombreEmpresa = empresaConfigService.obtenerEmpresa().getNombreComercial();

            String titulo = "Sistema ERP";

            if (event.getTituloModulo() != null) {
                titulo += " - " + event.getTituloModulo();
            }

            // 🔥 Actualizar ventana
            stage.setTitle(titulo);

            log.info("Título actualizado por navegación: {}", titulo);
        });
    }

    @EventListener
    public void onEmpresaActualizada(EmpresaActualizadaEvent event) {

        Platform.runLater(() -> {

            // Aún no está lista la UI
            if (mainContent.getScene() == null) {
                return;
            }

            // Obtener Stage
            Stage stage = (Stage) mainContent.getScene().getWindow();

            // 🔥 Topbar: solo nombre de empresa
            lblTituloSistema.setText(event.getNuevoNombre());

            // Obtener módulo actual desde el título
            String tituloActual = stage.getTitle();
            String modulo = "";

            if (tituloActual.contains(" - ")) {
                modulo = tituloActual.substring(tituloActual.lastIndexOf(" - ") + 3);
            }

            // 🔥 Ventana: empresa + módulo actual
            String nuevoTitulo = "Sistema ERP " + event.getNuevoNombre();

            if (!modulo.isBlank()) {
                nuevoTitulo += " - " + modulo;
            }

            stage.setTitle(nuevoTitulo);

            log.info("Nombre de empresa actualizado dinámicamente: {}", nuevoTitulo);
        });
    }

}

