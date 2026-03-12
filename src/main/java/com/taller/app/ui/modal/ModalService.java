package com.taller.app.ui.modal;

import com.taller.app.ui.main.StageManager;
import com.taller.app.ui.navigation.ViewLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModalService {

    private final ViewLoader viewLoader;
    private final StageManager stageManager;

    public void openModal(String fxmlPath, String titulo) {

        Parent root = viewLoader.loadForModal(fxmlPath);

        Stage modal = new Stage();
        modal.setTitle(titulo);
        modal.initOwner(stageManager.getPrimaryStage());
        modal.initModality(Modality.WINDOW_MODAL);
        modal.setResizable(false);

        modal.setScene(new Scene(root));
        modal.showAndWait();
    }

}
