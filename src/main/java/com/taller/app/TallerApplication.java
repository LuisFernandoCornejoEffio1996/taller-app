package com.taller.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class TallerApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(TallerSpringBoot.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/login.fxml")
        );

        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        stage.setTitle("Taller - Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void stop() {
        context.close();
    }
}