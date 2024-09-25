package me.thomasstokes.game;

import java.util.List;

import me.thomasstokes.enums.Colour;

public interface GuessResultAndFeedback {
    boolean isGuessCorrect();
    List<Colour> getFeedback();
    boolean isGameOver();
    default void setGameOver(boolean gameOver) {}
}
