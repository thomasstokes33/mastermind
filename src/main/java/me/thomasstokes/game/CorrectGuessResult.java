package me.thomasstokes.game;

import java.util.Collections;
import java.util.List;

import me.thomasstokes.App;
import me.thomasstokes.enums.Colour;

public class CorrectGuessResult implements GuessResultAndFeedback{
    public boolean isGuessCorrect() {
        return true;
    }
    public List<Colour> getFeedback() {
       return Collections.nCopies(App.PINS_PER_GUESS, Colour.CORRECT_COLOUR_AND_POSITION_RESULT_COLOUR);
    }
    public boolean isGameOver() {
        return true;
    }

}
