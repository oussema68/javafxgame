package org.example.puzzle;

import java.util.List;

public interface State {
    boolean isGoal();
    List<PuzzleState> generateNextStates();
    State getPreviousState();
}
