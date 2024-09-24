package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class FrontPageController {

    @FXML
    private Button startButton;

    @FXML
    private Button highScoresButton;



    @FXML
    public void initialize() {
        try {
            HighScoresController.loadHighScores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void startGame() {
        try {
            SceneManager.getInstance().loadScene("/board.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewHighScores() {
        try {
            initialize();
            SceneManager.getInstance().loadScene("/high_scores.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
