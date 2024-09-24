package org.example;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Random;

/**
 * Represents the game board where the player moves a stone through cells with various values.
 * The objective is to reach the goal while navigating framed cells and making valid moves.
 */
public class GameBoard {
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

    /**
     * Constructs a new {@code GameBoard} with the specified size, grid pane, and info label.
     *
     * @param size the size of the game board
     * @param gridPane the {@link GridPane} to display the board
     * @param infoLabel the {@link Label} to display information
     */
    public GameBoard(int size, GridPane gridPane, Label infoLabel) {
        this.size = size;
        this.gridPane = gridPane;
        this.infoLabel = infoLabel;
        this.board = new int[size][size];
        this.framed = new boolean[size][size];
        this.stonePositionX = 0;
        this.stonePositionY = 0;
        this.isFramed = false; // Initially not framed
        this.random = new Random();

        boolean solvable = false;
        while (!solvable) {
            initializeBoardValues();
            updateBoard();
            if (isSolvable(board)) {
                solvable = true;
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
        // 8x8 board specific solvability check
        return (emptyRowIndex % 2 == 0 && inversions % 2 != 0) || (emptyRowIndex % 2 != 0 && inversions % 2 == 0);
    }




    /** public GameBoard(int[][] boardState) {
        this.board = boardState;
        // Initialize stone positions based on the board state
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState[i].length; j++) {
                if (boardState[i][j] == 0) { // Assuming 0 represents the stone
                    stonePositionX = j;
                    stonePositionY = i;
                }
            }
        }
    }

     */

    /**
     * Initializes the board with random values and randomly frames some squares.
     */
    private void initializeBoardValues() {
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Check if the cell is within the inner 4x4 box
                if (i >= (size / 2 - 2) && i < (size / 2 + 2) && j >= (size / 2 - 2) && j < (size / 2 + 2)) {
                    board[i][j] = random.nextInt(4) + 1; // Random values between 1 and 4
                } else {
                    board[i][j] = random.nextInt(7) + 1; // Random values between 1 and 7
                }
                framed[i][j] = random.nextBoolean(); // Randomly frame squares
            }
        }
    }

    /**
     * Moves the stone by the specified deltas and updates the board.
     *
     * @param dx the change in the x-coordinate
     * @param dy the change in the y-coordinate
     */
    public void moveStone(int dx, int dy) {
        if (isValidMove(dx, dy)) {
            stonePositionX += dx;
            stonePositionY += dy;
            BoardController.incrementNumberOfMoves();
            updateBoard();
        } else {
            lose();
        }
    }

    /**
     * Checks if the move is valid.
     *
     * @param dx the change in the x-coordinate
     * @param dy the change in the y-coordinate
     * @return {@code true} if the move is valid, {@code false} otherwise
     */
    public static boolean isValidMove(int dx, int dy) {
        int newX = stonePositionX + dx;
        int newY = stonePositionY + dy;
        return newX >= 0 && newX < size && newY >= 0 && newY < size;
    }

    /**
     * Updates the board to reflect the current state, including the stone's position and framing.
     */
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
        stoneButton.setStyle("-fx-background-color: lightblue;" );


        gridPane.add(stoneButton, stonePositionX, stonePositionY);
        isFramed = framed[stonePositionY][stonePositionX];
        infoLabel.setText("Current value: " + board[stonePositionY][stonePositionX] + ", Framed: " + isFramed);

        if (checkWinCondition()) {
            infoLabel.setText("Congratulations! You've reached the goal!");
            if (this.boardController != null) {
                this.boardController.gameOver();
            } else {
                infoLabel.setText("congrats ama tneket");
            }

        }
    }

    /**
     * Checks if the player has won by reaching the goal position.
     *
     * @return {@code true} if the player has reached the goal, {@code false} otherwise
     */
    public boolean checkWinCondition() {
        return stonePositionX == size - 1 && stonePositionY == size - 1;
    }

    /**
     * Handles the loss condition when the player moves out of the board.
     */
    private void lose() {
        infoLabel.setText("You've moved out of the board! Game Over!");
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
