package com.taller.app.ui.main;

import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class StageManager {

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

