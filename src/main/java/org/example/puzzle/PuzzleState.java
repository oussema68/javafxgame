package org.example.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PuzzleState implements State {
    private static final Logger LOGGER = Logger.getLogger(PuzzleState.class.getName());
    private final int[][] board;
    private PuzzleState previousState;
    private int stonePositionX;
    private int stonePositionY;

    public PuzzleState(int[][] board) {
        this.board = board;

        // Find the stone (0) position
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    stonePositionX = i;
                    stonePositionY = j;
                    LOGGER.log(Level.INFO, "Stone found at position ({0}, {1})", new Object[]{stonePositionX, stonePositionY});
                }
            }
        }
    }

    public boolean isGoal() {
        boolean goalReached = stonePositionX == board.length - 1 && stonePositionY == board[0].length - 1;
        LOGGER.log(Level.INFO, "Goal state check: {0}", goalReached);
        return goalReached;
    }

    // This method creates a deep copy of the current board state
    private int[][] copyBoardState(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, board[i].length);
        }
        LOGGER.log(Level.FINE, "Created a copy of the board state");
        return newBoard;
    }

    public List<PuzzleState> generateNextStates() {
        List<PuzzleState> nextStates = new ArrayList<>();
        int currentX = getStonePositionX();
        int currentY = getStonePositionY();

        // If the current stone is 0, we return no possible moves (error handling)
        int stoneValue = board[currentX][currentY];
        if (stoneValue == 0) {
            LOGGER.log(Level.SEVERE, "Error: The stone cannot have a value of 0.");
            return nextStates;
        }

        LOGGER.log(Level.INFO, "Generating next states for stone at ({0}, {1}) with value {2}", new Object[]{currentX, currentY, stoneValue});

        // Movement directions for horizontal/vertical or diagonal
        int[][] directions;
        if (isFramed(currentX, currentY)) {
            // Framed squares: diagonal movement only
            directions = new int[][]{
                    {1, 1},   // down-right
                    {-1, -1}, // up-left
                    {1, -1},  // down-left
                    {-1, 1}   // up-right
            };
            LOGGER.log(Level.INFO, "Framed square - diagonal movement only.");
        } else {
            // Unframed squares: horizontal and vertical movement only
            directions = new int[][]{
                    {1, 0},   // right
                    {-1, 0},  // left
                    {0, 1},   // down
                    {0, -1}   // up
            };
            LOGGER.log(Level.INFO, "Unframed square - horizontal/vertical movement.");
        }

        // Try moving in all possible directions based on stone value
        for (int[] direction : directions) {
            int newX = currentX + direction[0] * stoneValue;
            int newY = currentY + direction[1] * stoneValue;

            // Check if the move is valid
            if (isValidMove(newX, newY)) {
                int[][] newBoardState = copyBoardState(board);
                newBoardState[currentX][currentY] = 0; // Set the current position to 0 (empty)
                newBoardState[newX][newY] = stoneValue; // Move the stone to the new position

                PuzzleState nextState = new PuzzleState(newBoardState);
                nextState.setPreviousState(this);
                nextState.setStonePosition(newX, newY); // Update the stone position
                nextStates.add(nextState);

                LOGGER.log(Level.INFO, "Valid move to ({0}, {1}) generated a new state.", new Object[]{newX, newY});
            } else {
                LOGGER.log(Level.WARNING, "Invalid move attempted to ({0}, {1})", new Object[]{newX, newY});
            }
        }

        LOGGER.log(Level.INFO, "Generated {0} next states.", nextStates.size());
        return nextStates;
    }

    // Helper function to check if the move stays within board boundaries
    private boolean isValidMove(int x, int y) {
        boolean valid = x >= 0 && x < board.length && y >= 0 && y < board[0].length;
        LOGGER.log(Level.FINE, "Move to ({0}, {1}) is {2}", new Object[]{x, y, valid ? "valid" : "invalid"});
        return valid;
    }

    // Method to check if a square is framed (example implementation)
    private boolean isFramed(int x, int y) {
        // Define framed squares based on your board logic
        // This could be based on the position or other criteria
        boolean framed = (x == 0 || x == board.length - 1 || y == 0 || y == board[0].length - 1);
        LOGGER.log(Level.FINE, "Square ({0}, {1}) is {2}", new Object[]{x, y, framed ? "framed" : "unframed"});
        return framed;
    }

    private void setStonePosition(int newX, int newY) {
        this.stonePositionX = newX;
        this.stonePositionY = newY;
        LOGGER.log(Level.FINE, "Stone position updated to ({0}, {1})", new Object[]{newX, newY});
    }

    public int getStonePositionX() {
        return stonePositionX;
    }

    public int getStonePositionY() {
        return stonePositionY;
    }

    public PuzzleState getPreviousState() {
        return previousState;
    }

    public void setPreviousState(PuzzleState previousState) {
        this.previousState = previousState;
    }

    public static PuzzleState fromBoardState(int[][] boardState) {
        return new PuzzleState(boardState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stonePositionX, stonePositionY, Arrays.deepHashCode(board));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PuzzleState other = (PuzzleState) obj;
        return stonePositionX == other.stonePositionX &&
                stonePositionY == other.stonePositionY &&
                Arrays.deepEquals(board, other.board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stone Position: (").append(stonePositionX).append(", ").append(stonePositionY).append(")\n");
        for (int[] row : board) {
            for (int cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        LOGGER.log(Level.FINE, "Board state:\n{0}", sb.toString());
        return sb.toString();
    }
}
