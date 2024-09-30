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
        displayWelcomeMessage();

        loadGameScene();
    }

    private void displayWelcomeMessage() {
        /** Create an alert dialog for the welcome message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Welcome to the Game!");
        alert.setHeaderText("how to frame it"); */
        welcomeLabel.setText("""
            Objective: Move the stone from the top-left to the bottom-right corner marked with an *!
            How to Play:

            Each square has a number indicating how many spaces you can move.
            If you land on a framed square, you can only move diagonally until you reach an unframed square.
            You can't move outside the board.
            Strategy Tips:
            Plan your moves to avoid framed squares.
            Use diagonal moves wisely!
            Click on Ok when you're ready!
            Have fun navigating the board!""");

        /** Disable the OK button initially
        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);



        // Create a PauseTransition to wait for 30 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(15));
        pause.setOnFinished(event -> {
            // Enable the OK button after the wait
            alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);


        });

        // Show the alert and start the pause
        alert.show();
        pause.play();  */
    }

    private void loadGameScene() {
        try {
            SceneManager.getInstance().loadScene("/board.fxml");
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
