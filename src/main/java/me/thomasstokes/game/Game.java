package me.thomasstokes.game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.thomasstokes.App;
import me.thomasstokes.enums.Colour;
public class Game {
    private final List<List<Colour>> guesses;
    private  List<Colour> answer;
    private int numberOfGuesses = 0;
    public Game() {
        guesses = new ArrayList<>(App.MAX_GUESSES);
        answer = new ArrayList<>(4);
        for (int i = 0; i < App.PINS_PER_GUESS; i++) {
            answer.add(Colour.randomColour());
        }
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
 * @param theGuess
 */
    public boolean guess(List<Colour> theGuess) {
        guesses.add(theGuess);
        numberOfGuesses++;
        return guessedCorrectly(theGuess);
    }

    public boolean guessedCorrectly(List<Colour> theGuess) {
        if (theGuess != null && answer != null) {
            return (theGuess.equals(answer));
        } else {
            return false;
        }
    }

    public int getNumOfGuesses() {
        return numberOfGuesses;
    }
}
 