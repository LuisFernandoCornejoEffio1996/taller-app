package com.taller.app.launcher;

import com.taller.app.ui.navigation.ViewLoader;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext context;


    @Override
    public void init() {

        context = new SpringApplicationBuilder(SpringBootStarter.class)
                .run();

    }

    @Override
    public void start(Stage stage) {

        ViewLoader loader = context.getBean(ViewLoader.class);

        Parent root = loader.load("/views/layout/mainLayout.fxml");

        Scene scene = new Scene(root);

        stage.setTitle("Taller ERP");
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();

    }

    @Override
    public void stop() {

        context.close();

    }
}
