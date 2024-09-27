package me.thomasstokes.game;

import java.util.List;

import me.thomasstokes.enums.Colour;

public class IncorrectGuessResult implements GuessResultAndFeedback{
    private List<Colour> guessFeedback;
    /*
     * Indicates whether the game is over. Default of false.
     */
    private boolean gameOver;
    public IncorrectGuessResult(List<Colour> guessFeedback) {
        this.guessFeedback = guessFeedback;
        gameOver = false;
    }

    public List<Colour> getFeedback() {
        return guessFeedback;
    }
    public boolean isGuessCorrect() {
      return false;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
