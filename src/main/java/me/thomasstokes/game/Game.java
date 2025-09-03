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
    /**
     * @return Returns the random List of colours corresponding to a guess.
     */
    public List<Colour> getRandomAnswer() {
        List<Colour> output = new ArrayList<>(App.PINS_PER_GUESS);
        for (int i = 0; i < App.PINS_PER_GUESS; i++) {
            output.add(Colour.randomColour());
        }
        return output;
    }
    /**
     * Converts the given List of Color enums into a user readable string.
     * @param guess A list of Color enums.
     * @return Returns a string to represent the given guess.
     */
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
     * Checks if the guess is correct and end or continue the game accordingly. Also returns the feedback of the Guess.
     * @param theGuess The list of colours representing a user's guess.
     * @return Returns a {@link GuessResultAndFeedback} object.
     */
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
     * Handles a game over.
     * @param isGameOverVictory True if the user has correctly guessed the answer.
     */
    public void gameOver(boolean isGameOverVictory) {
        gameOver = true;
        for (GameOverListener gameOverListener : gameOverListeners) {
            gameOverListener.gameOver(isGameOverVictory, answer);
        }
    }
    /**
     * Calculates the feedback for a given guess.
     * {@link Colour#CORRECT_COLOUR_CORRECT_POSITION_RESULT_COLOUR} indicates there is a pin with the correct colour
     * that is in the correct position, while {@link Colour#CORRECT_COLOUR_WRONG_POSITION_RESULT_COLOUR} indicates 
     * there is a pin that is the correct colour, but it is in the wrong position.
     * @param theGuess The user's guess.
     * @return  Returns a {@link GuessResultAndFeedback} object.
     */
    private GuessResultAndFeedback getFeedbackForGuess(List<Colour> theGuess) {
        List<Colour> feedback = new ArrayList<>(App.PINS_PER_GUESS);
        Colour[] guessArray = theGuess.toArray(new Colour[App.PINS_PER_GUESS]);
        Colour[] answerArray = answer.toArray(new Colour[App.PINS_PER_GUESS]);
        int correctPositionAndColourCount = 0;
        // sweep for pins that are the correct colour and in the correct position.
        for (int i = 0; i < answerArray.length; i++) {
            if (guessArray[i] == answerArray[i]) {
                feedback.add(Colour.CORRECT_COLOUR_CORRECT_POSITION_RESULT_COLOUR);
                guessArray[i] = Colour.NONE;
                answerArray[i] = Colour.NONE;
                correctPositionAndColourCount++;
            }
        }
        GuessResultAndFeedback guessResult;
        if (correctPositionAndColourCount == App.PINS_PER_GUESS) {
            guessResult = new CorrectGuessResult();
        } else {
            // sweep for pins that are the correct colour but wrong position.
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
 