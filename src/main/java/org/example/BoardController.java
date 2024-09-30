package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.example.puzzle.PuzzleState;
import org.example.puzzle.SolverIntegration;
import org.example.puzzle.State;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class BoardController {

    private static BoardController boardController;
    private static final Logger logger = Logger.getLogger(BoardController.class.getName());

    @FXML
    private GridPane boardGrid;

    @FXML
    private Label infoLabel;

    @FXML
    private Button back;

    @FXML
    private Button Solve;

    private GameBoard gameBoard;
    private static int numberOfMoves;

    @FXML
    public void initialize() {
        logger.info("Initializing BoardController...");

        // Create the GameBoard object, passing 'this' as the BoardController
        gameBoard = new GameBoard(8, boardGrid, infoLabel, this);

        numberOfMoves = 0;

        // Ensure no gaps between cells
        boardGrid.setHgap(0);
        boardGrid.setVgap(0);
        boardGrid.setPadding(new Insets(0, 0, 0, 0));

        logger.info("Board initialized with 8x8 grid.");
    }


    @FXML
    private void solvePuzzle() {
        logger.info("Solve button clicked");
        SolverIntegration solver = new SolverIntegration();
        int[][] boardState = gameBoard.getBoardState(); // Get current board state
        logger.info("Current board state: " + Arrays.deepToString(boardState));

        PuzzleState initialState = PuzzleState.fromBoardState(boardState); // Convert to PuzzleState
        List<State> solution = solver.solvePuzzle(initialState); // Get the solution steps

        logger.info("Solution found with " + solution.size() + " steps.");

        // Execute the solution steps
        for (State state : solution) {
            PuzzleState puzzleState = (PuzzleState) state;
            int x = puzzleState.getStonePositionX();
            int y = puzzleState.getStonePositionY();
            logger.info("Moving stone to: (" + x + ", " + y + ")");
            gameBoard.moveStone(x - gameBoard.getStonePositionX(), y - gameBoard.getStonePositionY());
        }
    }

    @FXML
    private void moveRight() {
        logger.info("Move right action triggered.");
        gameBoard.moveRight();
    }

    @FXML
    private void moveLeft() {
        logger.info("Move left action triggered.");
        gameBoard.moveLeft();
    }

    @FXML
    private void moveUp() {
        logger.info("Move up action triggered.");
        gameBoard.moveUp();
    }

    @FXML
    private void moveDown() {
        logger.info("Move down action triggered.");
        gameBoard.moveDown();
    }

    @FXML
    private void moveUpLeft() {
        logger.info("Move up-left action triggered.");
        gameBoard.moveUpLeft();
    }

    @FXML
    private void moveUpRight() {
        logger.info("Move up-right action triggered.");
        gameBoard.moveUpRight();
    }

    @FXML
    private void moveDownLeft() {
        logger.info("Move down-left action triggered.");
        gameBoard.moveDownLeft();
    }

    @FXML
    private void moveDownRight() {
        logger.info("Move down-right action triggered.");
        gameBoard.moveDownRight();
    }

    @FXML
    void backToMenu() throws IOException {
        logger.info("Back to menu button clicked.");
        SceneManager.getInstance().loadScene("/front_page.fxml");
    }

    public static void incrementNumberOfMoves() {
        numberOfMoves++;
        logger.info("Number of moves incremented: " + numberOfMoves);
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void gameOver() {
        int numberOfMoves = getNumberOfMoves();
        logger.info("Game over. Total moves: " + numberOfMoves);
        // Ensure this method is called to save high scores
            // Assuming this updates the game state after win
        HighScoresController.addHighScore(numberOfMoves);



    }
}
