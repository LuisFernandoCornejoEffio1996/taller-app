package com.taller.app.modules.dashboard.ui;

import com.taller.app.ui.navigation.NavigationService;
import com.taller.app.ui.navigation.Views;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DashboardController {

    private final NavigationService navigationService;

    @FXML
    public void initialize(){

        log.info("Dashboard cargado");

    }

    @FXML
    private void abrirEmpresa(){

        navigationService.navigateTo(Views.EMPRESA_CONFIG);
        log.info("Abrir empresa desde dashboard");

    }

    @FXML
    private void abrirUnidadMedida(){

        navigationService.navigateTo(Views.UNIDAD_MEDIDA_LIST);
        log.info("Abrir unidad de medida desde dashboard");

    }
}
