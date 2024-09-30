package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import java.util.logging.Logger;

public class HighScoresController {

    @FXML
    private Label highScoresLabel;

    @FXML
    private Button backToMenuButton;

    private static final String HIGH_SCORES_FILE = "high_scores.json";
    private static final List<Integer> highScores = new ArrayList<>();

    // Logger to track all events
    private static final Logger logger = Logger.getLogger(HighScoresController.class.getName());

    @FXML
    public void initialize() {
        logger.info("Initializing HighScoresController...");
        try {
            loadHighScores();
            initializeHighScores(highScores);
            logger.info("High scores successfully loaded and initialized.");
        } catch (IOException e) {
            logger.severe("Failed to load high scores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initializeHighScores(List<Integer> highScores) {
        logger.info("Initializing high scores label...");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < highScores.size(); i++) {
            sb.append("High Score ").append(i + 1).append(": ").append(highScores.get(i)).append("\n");
        }
        highScoresLabel.setText(sb.toString());
        logger.info("High scores label updated.");
    }

    @FXML
    void backToMenu() throws IOException {
        logger.info("Back to menu button clicked.");
        SceneManager.getInstance().loadScene("/front_page.fxml");
        logger.info("Navigated back to the main menu.");
    }

    static void loadHighScores() throws IOException {
        File file = new File(HIGH_SCORES_FILE);
        if (!file.exists()) {
            logger.warning("High scores file does not exist. Creating a new file...");
            // If the file does not exist, create it with an empty high scores list
            saveHighScores();
        } else {
            logger.info("High scores file found. Loading...");
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type highScoresType = new TypeToken<ArrayList<Integer>>() {}.getType();
            List<Integer> loadedHighScores = gson.fromJson(reader, highScoresType);

            if (loadedHighScores != null) {
                highScores.clear();
                highScores.addAll(loadedHighScores);
                logger.info("High scores successfully loaded from file: " + loadedHighScores);
            } else {
                logger.warning("No high scores found in file.");
            }
            // Parse the JSON file into a JsonElement
            JsonElement jsonElement = JsonParser.parseReader(reader);

            // Pretty print the JSON content
            String prettyJson = gson.toJson(jsonElement);
            System.out.println("json file content: " + prettyJson);
        } catch (IOException e) {
            logger.severe("Error reading the high scores file: " + e.getMessage());
            throw e;
        }

    }

    public static void saveHighScores() {
        logger.info("Saving high scores to file...");
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(HIGH_SCORES_FILE)) {
            gson.toJson(highScores, writer);
            logger.info("High scores saved successfully: " + highScores);
        } catch (IOException e) {
            logger.severe("Failed to save high scores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addHighScore(int score) {
        logger.info("Adding new high score: " + score);
        highScores.add(score);
        saveHighScores();

    }

    public static List<Integer> getHighScores() {
        logger.info("Fetching high scores...");
        return highScores;
    }
}
