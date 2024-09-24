package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HighScoresController {

    @FXML
    private Label highScoresLabel;

    @FXML
    private Button backToMenuButton;

    private static final String HIGH_SCORES_FILE = "high_scores.json";
    private static final List<Integer> highScores = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            loadHighScores();
            initializeHighScores(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeHighScores(List<Integer> highScores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < highScores.size(); i++) {
            sb.append("High Score ").append(i + 1).append(": ").append(highScores.get(i)).append("\n");
        }
        highScoresLabel.setText(sb.toString());
    }

    @FXML
    void backToMenu() throws IOException {
        SceneManager.getInstance().loadScene("/front_page.fxml");
    }

    static void loadHighScores() throws IOException {
        File file = new File(HIGH_SCORES_FILE);
        if (!file.exists()) {
            // If the file does not exist, create it with an empty high scores list
            saveHighScores();
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type highScoresType = new TypeToken<ArrayList<Integer>>() {}.getType();
            List<Integer> loadedHighScores = gson.fromJson(reader, highScoresType);
            if (loadedHighScores != null) {
                highScores.clear();
                highScores.addAll(loadedHighScores);
            }
        }
    }

    public static void saveHighScores() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(HIGH_SCORES_FILE)) {
            gson.toJson(highScores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addHighScore(int score) {
        highScores.add(score);
        saveHighScores();
    }

    public static List<Integer> getHighScores() {
        return highScores;
    }
}
