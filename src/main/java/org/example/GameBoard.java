package org.example;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the game board where the player moves a stone through cells with various values.
 * The objective is to reach the goal while navigating framed cells and making valid moves.
 */
public class GameBoard {
    private static final Logger LOGGER = Logger.getLogger(GameBoard.class.getName());
    public static boolean isFramed;
    public static int size = 0;
    private GridPane gridPane;
    public static int[][] board;
    private boolean[][] framed;
    private static int stonePositionX;
    private static int stonePositionY;
    private Random random;
    private Label infoLabel;
    private BoardController boardController;

    public GameBoard(int size, GridPane gridPane, Label infoLabel, BoardController boardController) {
        this.size = size;
        this.gridPane = gridPane;
        this.infoLabel = infoLabel;
        this.board = new int[size][size];
        this.framed = new boolean[size][size];
        this.stonePositionX = 0;
        this.stonePositionY = 0;
        this.isFramed = false; // Initially not framed
        this.random = new Random();
        this.boardController = boardController;  // Assign the boardController here

        LOGGER.log(Level.INFO, "Initializing GameBoard with size {0}x{0}", size);

        // Initialize and check if the board is solvable
        boolean solvable = false;
        while (!solvable) {
            initializeBoardValues();
            updateBoard();
            if (isSolvable(board)) {
                solvable = true;
                LOGGER.log(Level.INFO, "Board initialized and solvable");
            } else {
                LOGGER.log(Level.WARNING, "Generated board is not solvable, regenerating...");
            }
        }
    }


    private int countInversions(int[][] board) {
        int inversions = 0;
        int[] flattened = new int[GameBoard.size * GameBoard.size];
        int index = 0;

        // Flatten the board into a single array for easier inversion count
        for (int i = 0; i < GameBoard.size; i++) {
            for (int j = 0; j < GameBoard.size; j++) {
                flattened[index++] = board[i][j];
            }
        }

        // Count inversions
        for (int i = 0; i < flattened.length; i++) {
            for (int j = i + 1; j < flattened.length; j++) {
                if (flattened[i] != 0 && flattened[j] != 0 && flattened[i] > flattened[j]) {
                    inversions++;
                }
            }
        }

        LOGGER.log(Level.INFO, "Counted {0} inversions in the board", inversions);
        return inversions;
    }

    private boolean isSolvable(int[][] board) {
        int inversions = countInversions(board);

        // Find row index of empty cell
        int emptyRowIndex = -1;
        for (int i = 0; i < GameBoard.size; i++) {
            for (int j = 0; j < GameBoard.size; j++) {
                if (board[i][j] == 0) {
                    emptyRowIndex = GameBoard.size - i; // Convert to 1-indexed from bottom
                    break;
                }
            }
        }

        // Check solvability based on the number of inversions and the row index of the empty cell
        boolean solvable = (emptyRowIndex % 2 == 0 && inversions % 2 != 0) || (emptyRowIndex % 2 != 0 && inversions % 2 == 0);
        LOGGER.log(Level.INFO, "Board solvability: {0}", solvable);
        return solvable;
    }

    private void initializeBoardValues() {
        Random random = new Random();
        LOGGER.log(Level.INFO, "Initializing board values");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i >= (size / 2 - 2) && i < (size / 2 + 2) && j >= (size / 2 - 2) && j < (size / 2 + 2)) {
                    board[i][j] = random.nextInt(4) + 1; // Random values between 1 and 4
                } else {
                    board[i][j] = random.nextInt(7) + 1; // Random values between 1 and 7
                }
                framed[i][j] = random.nextBoolean(); // Randomly frame squares
                LOGGER.log(Level.FINE, "Cell [{0}, {1}] set to {2}, Framed: {3}", new Object[]{i, j, board[i][j], framed[i][j]});
            }
        }
    }

    public void moveStone(int dx, int dy) {
        if (isValidMove(dx, dy)) {
            stonePositionX += dx;
            stonePositionY += dy;
            BoardController.incrementNumberOfMoves();
            LOGGER.log(Level.INFO, "Moved stone to [{0}, {1}]", new Object[]{stonePositionX, stonePositionY});
            updateBoard();
        } else {
            LOGGER.log(Level.SEVERE, "Invalid move out of bounds to [{0}, {1}]", new Object[]{stonePositionX + dx, stonePositionY + dy});
            lose();
        }
    }

    public static boolean isValidMove(int dx, int dy) {
        int newX = stonePositionX + dx;
        int newY = stonePositionY + dy;
        boolean valid = newX >= 0 && newX < size && newY >= 0 && newY < size;
        LOGGER.log(Level.INFO, "Move to [{0}, {1}] is {2}", new Object[]{newX, newY, valid ? "valid" : "invalid"});
        return valid;
    }

    private void updateBoard() {
        gridPane.getChildren().clear();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button button = new Button(String.valueOf(board[i][j]));
                if (framed[i][j]) {
                    button.setStyle("-fx-border-color: black;");
                }
                gridPane.add(button, j, i);
            }
        }
        Button stoneButton = new Button(String.valueOf(board[stonePositionY][stonePositionX]));
        stoneButton.setStyle("-fx-background-color: lightblue;");
        gridPane.add(stoneButton, stonePositionX, stonePositionY);
        isFramed = framed[stonePositionY][stonePositionX];
        infoLabel.setText("Current value: " + board[stonePositionY][stonePositionX] + ", Framed: " + isFramed);

        LOGGER.log(Level.INFO, "Board updated. Stone at [{0}, {1}]", new Object[]{stonePositionX, stonePositionY});

        if (checkWinCondition()) {
            infoLabel.setText("Congratulations! You've reached the goal!");
            LOGGER.log(Level.INFO, "Game won!");
            if (this.boardController != null) {
                System.out.println("thers a value");
                this.boardController.gameOver();
            }
        }
    }

    public boolean checkWinCondition() {
        boolean won = stonePositionX == size - 1 && stonePositionY == size - 1;
        LOGGER.log(Level.INFO, "Check win condition: {0}", won);
        return won;
    }

    private void lose() {
        infoLabel.setText("You've moved out of the board! Game Over!");
        LOGGER.log(Level.SEVERE, "Game Over! Invalid move");
    }


/**
     * Moves the stone to the right by the number of steps in the current cell's value if not framed.
     */
    public void moveRight() {
        if (!isFramed) {
            int steps = board[stonePositionY][stonePositionX];
            moveStone(steps, 0);
        }
    }

    /**
     * Moves the stone to the left by the number of steps in the current cell's value if not framed.
     */
    public void moveLeft() {
        if (!isFramed) {
            int steps = board[stonePositionY][stonePositionX];
            moveStone(-steps, 0);
        }
    }

    /**
     * Moves the stone down by the number of steps in the current cell's value if not framed.
     */
    public void moveDown() {
        if (!isFramed) {
            int steps = board[stonePositionY][stonePositionX];
            moveStone(0, steps);
        }
    }

    /**
     * Moves the stone up by the number of steps in the current cell's value if not framed.
     */
    public void moveUp() {
        if (!isFramed) {
            int steps = board[stonePositionY][stonePositionX];
            moveStone(0, -steps);
        }
    }

    /**
     * Moves the stone diagonally up and to the left by the number of steps in the current cell's value if framed.
     */
    public void moveUpLeft() {
        if (isFramed) {
            int steps = board[stonePositionY][stonePositionX];
            moveStone(-steps, -steps);
        }
    }

    /**
     * Moves the stone diagonally up and to the right by the number of steps in the current cell's value if framed.
     */
    public void moveUpRight() {
        if (isFramed) {
            int steps = board[stonePositionY][stonePositionX];
            moveStone(steps, -steps);
        }
    }

    /**
     * Moves the stone diagonally down and to the left by the number of steps in the current cell's value if framed.
     */
    public void moveDownLeft() {
        if (isFramed) {
            int steps = board[stonePositionY][stonePositionX];
            moveStone(-steps, steps);
        }
    }

    /**
     * Moves the stone diagonally down and to the right by the number of steps in the current cell's value if framed.
     */
    public void moveDownRight() {
        if (isFramed) {
            int steps = board[stonePositionY][stonePositionX];
            moveStone(steps, steps);
        }
    }

    /**
     * Gets the current x-coordinate of the stone.
     *
     * @return the x-coordinate of the stone
     */
    public int getStonePositionX() {
        return stonePositionX;
    }

    /**
     * Gets the current y-coordinate of the stone.
     *
     * @return the y-coordinate of the stone
     */
    public int getStonePositionY() {
        return stonePositionY;
    }

    /**
     * Gets the current state of the board.
     *
     * @return the state of the board as a 2D array of integers
     */



    public int[][] getBoardState() {
        return board;
    }
}
