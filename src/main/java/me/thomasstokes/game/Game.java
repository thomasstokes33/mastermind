package me.thomasstokes.game;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.thomasstokes.App;
import me.thomasstokes.enums.Colour;
import me.thomasstokes.listeners.GameOverListener;
public class Game {
    private static final Logger logger = LogManager.getLogger(Game.class);
    private final List<GameOverListener> gameOverListeners;
    private final List<List<Colour>> guesses;
    private final List<List<Colour>> feedback;
    private  List<Colour> answer;
    private int numberOfGuesses = 0;
    private boolean gameOver;
    public Game() {
        gameOver = false;
        guesses = new ArrayList<>(App.MAX_GUESSES);
        feedback = new ArrayList<>(App.MAX_GUESSES);
        gameOverListeners = new ArrayList<>();
        answer = getRandomAnswer();
        logger.info("Answer for game " + this + " is " + guessToString(answer));
    }
    public List<Colour> getRandomAnswer() {
        List<Colour> output = new ArrayList<>(App.PINS_PER_GUESS);
        for (int i = 0; i < App.PINS_PER_GUESS; i++) {
            output.add(Colour.randomColour());
        }
        return output;
    }
    public static String guessToString(List<Colour> guess) {
        StringBuilder output = new StringBuilder();
        for (Colour colour : guess) {
            output.append(colour.toString());
            output.append(' ');
        }
        return output.toString();
    }

/**
 * Sets passed index (indexing starts at 0) to the passed guess.
 */
    public void setGuess(int guess, List<Colour> theGuess) {
        if (guess < numberOfGuesses) {
            guesses.set(guess, theGuess);
        }
    }
/**
 * Adds guess to list of guesses
 * @return Returns true if the guess is correct (and false otherwise).
 **/
    public boolean guess(List<Colour> theGuess) {
        guesses.add(theGuess);
        numberOfGuesses++;
    public void addGameOverListener(GameOverListener gameOverListener) {
        gameOverListeners.add(gameOverListener);
    }

    /**
     * 
     * @param isGameOverVictory True if the game is over in the victory state.
     */
    public void gameOver(boolean isGameOverVictory) {
        gameOver = true;
        for (GameOverListener gameOverListener : gameOverListeners) {
            gameOverListener.gameOver(isGameOverVictory, answer);
        }
    }
    }

    /**
     * @return Returns true if the passed guess is 
     * the correct answer.
     */
    private boolean guessedCorrectly(List<Colour> theGuess) {
        if (theGuess != null && answer != null) {
            return (theGuess.equals(answer));
        } else {
            return false;
        }
    }

    public int getNumOfGuesses() {
        return numberOfGuesses;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
 