package me.thomasstokes.game;

import java.util.List;

import me.thomasstokes.enums.Colour;
import me.thomasstokes.App;
/**
 * An interface for an object that can store feedback for a guess and indicate if the guess is correct and also if the game is over.
 * Note: we differentiate between a correct guess and a guess that leads to game over, because an incorrect guess can 
 * lead to a game over if it is the last guess (see {@link App#MAX_GUESSES}).
 */
public interface GuessResultAndFeedback {
    boolean isGuessCorrect();
    List<Colour> getFeedback();
    /**
     * 
     * @return Returns true if the game is over after receiving this guess or not.
     */
    boolean isGameOver();
    default void setGameOver(boolean gameOver) {}
}
