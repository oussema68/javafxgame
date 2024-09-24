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

public class BoardController {

    private static BoardController boardController;
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
        gameBoard = new GameBoard(8, boardGrid, infoLabel);
        numberOfMoves = 0;

        // Ensure no gaps between cells
        boardGrid.setHgap(0);
        boardGrid.setVgap(0);
        boardGrid.setPadding(new Insets(0, 0, 0, 0));
        this.boardController = this;
    }






    @FXML
    private void solvePuzzle() {
        System.out.println("Solve button clicked"); // Log statement
        SolverIntegration solver = new SolverIntegration();
        int[][] boardState = gameBoard.getBoardState(); // Get current board state
        System.out.println("Current board state: " + Arrays.deepToString(boardState)); // Log statement
        PuzzleState initialState = PuzzleState.fromBoardState(boardState); // Convert to PuzzleState
        List<State> solution = solver.solvePuzzle(initialState); // Get the solution steps
        System.out.println("Solution found: " + solution.size() + " steps"); // Log statement

        // Execute the solution steps
        for (State state : solution) {
            PuzzleState puzzleState = (PuzzleState) state;
            int x = puzzleState.getStonePositionX();
            int y = puzzleState.getStonePositionY();
            System.out.println("Moving stone to: (" + x + ", " + y + ")"); // Log statement
            gameBoard.moveStone(x - gameBoard.getStonePositionX(), y - gameBoard.getStonePositionY());
        }
    }

    @FXML
    private void moveRight() {
        gameBoard.moveRight();
    }

    @FXML
    private void moveLeft() {
        gameBoard.moveLeft();
    }

    @FXML
    private void moveUp() {
        gameBoard.moveUp();
    }

    @FXML
    private void moveDown() {
        gameBoard.moveDown();
    }

    @FXML
    private void moveUpLeft() {
        gameBoard.moveUpLeft();
    }

    @FXML
    private void moveUpRight() {
        gameBoard.moveUpRight();
    }

    @FXML
    private void moveDownLeft() {
        gameBoard.moveDownLeft();
    }

    @FXML
    private void moveDownRight() {
        gameBoard.moveDownRight();
    }

    @FXML

    void backToMenu() throws IOException {
        SceneManager.getInstance().loadScene("/front_page.fxml");
    }



    public static void incrementNumberOfMoves() {
        numberOfMoves++;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void gameOver() {
        int numberOfMoves = getNumberOfMoves();
        HighScoresController.addHighScore(numberOfMoves);

    }
}

