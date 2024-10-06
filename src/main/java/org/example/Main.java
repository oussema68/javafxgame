package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Set up the SceneManager
        SceneManager.getInstance().setStage(primaryStage);

        // Load the initial scene
        SceneManager.getInstance().loadScene("/front_page.fxml");

        // Set and show the stage

        primaryStage.setTitle("Frame It");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
.