package me.thomasstokes.component;

import java.util.Collections;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import me.thomasstokes.App;
import me.thomasstokes.enums.Colour;

/**
 * The final row that hides the answer from the user until the end.
 */
public class AnswerRevealedRow extends HBox {
    /**
     * The nested HBox that actually contains the result.
     */
    HBox resultArea = new HBox();
    private final String GAME_OVER_CSS_CLASS_NAME = "game-over";
    public AnswerRevealedRow() {
        getStyleClass().add("answer-revealed-row");
        getChildren().add(resultArea);
        resultArea.getStyleClass().add("result-area");
        populateWithGuess(Collections.nCopies(App.PINS_PER_GUESS, Colour.HIDDEN_PIN_COLOUR), false);
    }
    public AnswerRevealedRow(List<Colour> answer, boolean gameOver) {
        this();
        populateWithGuess(answer, gameOver);
    }

    public void populateWithGuess(List<Colour> answer, boolean gameOver) {
        resultArea.getChildren().clear();
        for (Colour colour : answer) {
            Button resultPin = new Button();
            resultPin.getStyleClass().add(colour.toString());
            resultArea.getChildren().add(resultPin);
        }
        if (gameOver) {
            resultArea.getStyleClass().add(GAME_OVER_CSS_CLASS_NAME);
        } else {
            resultArea.getStyleClass().remove(GAME_OVER_CSS_CLASS_NAME);
        }
    }
    
}
