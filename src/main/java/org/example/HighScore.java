package org.example;

public class HighScore implements Comparable<HighScore> {
    private String name;
    private int score;

    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(HighScore other) {
        // Sort by score in ascending order (less is better)
        return Integer.compare(this.score, other.score);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Score: " + score;
    }
}
