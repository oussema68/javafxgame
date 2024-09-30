package org.example.puzzle;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolverIntegration {

    private static final Logger LOGGER = Logger.getLogger(SolverIntegration.class.getName());

    public List<State> solvePuzzle(State initialState) {
        LOGGER.log(Level.INFO, "Starting puzzle solver with initial state.");
        BreadthFirstSearch bfs = new BreadthFirstSearch();
        List<State> solution = bfs.search((PuzzleState) initialState);
        if (solution.isEmpty()) {
            LOGGER.log(Level.WARNING, "No solution found for the puzzle.");
        } else {
            LOGGER.log(Level.INFO, "Solution found with {0} steps.", solution.size());
        }
        return solution;
    }

    private static class BreadthFirstSearch {
        public List<State> search(PuzzleState initialState) {
            Queue<PuzzleState> queue = new LinkedList<>();
            Set<PuzzleState> visited = new HashSet<>();
            queue.add(initialState);
            visited.add(initialState);

            LOGGER.log(Level.INFO, "Starting BFS with initial state:\n{0}", initialState);

            while (!queue.isEmpty()) {
                PuzzleState currentState = queue.poll();
                LOGGER.log(Level.FINE, "Processing state:\n{0}", currentState);

                if (currentState.isGoal()) {
                    LOGGER.log(Level.INFO, "Goal state reached:\n{0}", currentState);
                    return reconstructPath(currentState);
                }

                for (PuzzleState nextState : currentState.generateNextStates()) {
                    if (!visited.contains(nextState)) {
                        nextState.setPreviousState(currentState);
                        visited.add(nextState);
                        queue.add(nextState);
                        LOGGER.log(Level.FINE, "Added next state to queue:\n{0}", nextState);
                    }
                }
            }

            LOGGER.log(Level.WARNING, "BFS ended - no solution found.");
            return Collections.emptyList();
        }

        private List<State> reconstructPath(PuzzleState goalState) {
            List<State> path = new ArrayList<>();
            PuzzleState current = goalState;
            LOGGER.log(Level.INFO, "Reconstructing path from goal state.");

            while (current != null) {
                path.add(current);
                current = current.getPreviousState();
            }
            Collections.reverse(path);

            LOGGER.log(Level.INFO, "Path reconstructed with {0} steps.", path.size());
            return path;
        }
    }
}
