package org.example.puzzle;

import java.util.*;

public class SolverIntegration {

    public List<State> solvePuzzle(State initialState) {
        BreadthFirstSearch bfs = new BreadthFirstSearch();
        return bfs.search((PuzzleState) initialState);
    }

    private static class BreadthFirstSearch {
        public List<State> search(PuzzleState initialState) {
            Queue<PuzzleState> queue = new LinkedList<>();
            Set<PuzzleState> visited = new HashSet<>();
            queue.add(initialState);
            visited.add(initialState);

            System.out.println("Starting BFS with initial state: " + initialState);

            while (!queue.isEmpty()) {
                PuzzleState currentState = queue.poll();
                System.out.println("Processing state: " + currentState);

                if (currentState.isGoal()) {
                    System.out.println("Goal state found: " + currentState);
                    return reconstructPath(currentState);
                }

                for (PuzzleState nextState : currentState.generateNextStates()) {
                    if (!visited.contains(nextState)) {
                        nextState.setPreviousState(currentState);
                        visited.add(nextState);
                        queue.add(nextState);
                        System.out.println("Adding next state: " + nextState);
                    }
                }
            }
            System.out.println("No solution found.");
            return Collections.emptyList();
        }


        private List<State> reconstructPath(PuzzleState goalState) {
            List<State> path = new ArrayList<>();
            PuzzleState current = goalState;

            while (current != null) {
                path.add(current);
                current = current.getPreviousState();
            }
            Collections.reverse(path);
            return path;
        }
    }
}
