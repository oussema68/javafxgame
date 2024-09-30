package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class InstructionsDialogController {


    Button go;

    @FXML
    public void start() {
        try {
            SceneManager.getInstance().loadScene("/board.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
