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

    public GuessResultAndFeedback guess(List<Colour> theGuess) {
        GuessResultAndFeedback guessResultAndFeedback = getFeedbackForGuess(theGuess);
        guesses.add(theGuess);
        feedback.add(guessResultAndFeedback.getFeedback());
        numberOfGuesses++;
        if  (guessResultAndFeedback.isGuessCorrect()) {  
            gameOver(true);
        } else if (!guessResultAndFeedback.isGuessCorrect() && numberOfGuesses == App.MAX_GUESSES) {
            guessResultAndFeedback.setGameOver(true);
            gameOver(false);
        }
        return guessResultAndFeedback;
    }
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

    private GuessResultAndFeedback getFeedbackForGuess(List<Colour> theGuess) {
        List<Colour> feedback = new ArrayList<>(App.PINS_PER_GUESS);
        Colour[] guessArray = theGuess.toArray(new Colour[App.PINS_PER_GUESS]);
        Colour[] answerArray = answer.toArray(new Colour[App.PINS_PER_GUESS]);
        int blackCount = 0;
        // black sweep
        for (int i = 0; i < answerArray.length; i++) {
            if (guessArray[i] == answerArray[i]) {
                feedback.add(Colour.CORRECT_COLOUR_AND_POSITION_RESULT_COLOUR);
                guessArray[i] = Colour.NONE;
                answerArray[i] = Colour.NONE;
                blackCount++;
            }
        }
        GuessResultAndFeedback guessResult;
        if (blackCount == App.PINS_PER_GUESS) {
            guessResult = new CorrectGuessResult();
        } else {
            // white sweep
            for (int guessPos = 0; guessPos < guessArray.length; guessPos++) {
                if (guessArray[guessPos] != Colour.NONE) {
                    for (int answerPos = 0; answerPos < answerArray.length; answerPos++) {
                        if (guessArray[guessPos] == answerArray[answerPos]) {
                            feedback.add(Colour.CORRECT_COLOUR_WRONG_POSITION_RESULT_COLOUR);
                            answerArray[answerPos] = Colour.NONE;
                            guessArray[guessPos] = Colour.NONE;
                            break; // Without this any White match would be comparing the guess position, now set to None,
                                    // with any other Nones in the answer which may result in more "false" whites.
                        }
                    }
                }
            }
            while (feedback.size() < App.PINS_PER_GUESS) {
                feedback.add(Colour.NONE);
            }
            guessResult = new IncorrectGuessResult(feedback);
        }
        logger.info("Feedback: " + guessResult.getFeedback()); 
        return guessResult;
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
 