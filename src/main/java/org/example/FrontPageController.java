package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class FrontPageController {

    @FXML
    private Button startButton;

    @FXML
    private Button highScoresButton;

    @FXML
    private Label welcomeLabel;

    // Initialize method called when the controller is loaded
    @FXML
    public void initialize() {
        try {
            HighScoresController.loadHighScores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method is linked to the "Start" button in your FXML
    @FXML
    private void startGame() {


        loadGameScene();
    }



    private void loadGameScene() {
        try {
            SceneManager.getInstance().loadScene("/InstructionsDialog.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewHighScores() {
        try {
            initialize(); // This might not be necessary here, depending on how you manage high scores
            SceneManager.getInstance().loadScene("/high_scores.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
