package com.taller.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class TallerApplication extends Application {

    @Getter
    private static ConfigurableApplicationContext context;
    private static Stage primaryStage;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(TallerSpringBoot.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {

        primaryStage = stage;
        cambiarVista("/views/main/MainLayout.fxml"); // Vista temporal
        stage.setTitle("Inicio Sesión");
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.centerOnScreen();
    }

    public static void cambiarVista(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(TallerApplication.class.getResource(rutaFXML));
            loader.setControllerFactory(context::getBean);

            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTitle(String titulo){
        if (primaryStage != null){
            primaryStage.setTitle("SISTEMA ERP - "+ titulo);
        }
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}