package me.thomasstokes.component;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.thomasstokes.App;
import me.thomasstokes.enums.Colour;
import me.thomasstokes.listeners.GuessConfirmedListener;

public class GuessRow extends HBox {
    private static final Logger logger = LogManager.getLogger(GuessRow.class);
    private List<GuessConfirmedListener> guessConfirmedListeners = new ArrayList<>();
    private HBox guessArea;
    /**
     * These are the buttons that act as the "pin holes", which change colour corresponding 
     * to a user's guesses. 
     */
    private final List<Button> colourChangeButtons;
    /**
     * Array storing the current guess.
     */
    private final List<Colour> guess;
    private boolean locked = false;
    private boolean enabled = false;
    /**
     * The selected position. Starts at 0.
     */
    private int selectedPosition = 0;
    public GuessRow() {
        guess = new ArrayList<>(App.PINS_PER_GUESS);
        for (int i = 0; i < App.PINS_PER_GUESS; i++) {
            guess.add(Colour.NONE);
        }
        colourChangeButtons = new ArrayList<>();
        getStyleClass().add("guess-row");
        build();
        setSelectedPosition(0);
    }

    public void build() {
        getChildren().clear();
        guessArea = new HBox();
        guessArea.getStyleClass().add("guess-area");
        getChildren().add(guessArea);
        for (int j = 0; j < App.PINS_PER_GUESS; j++) {
            var button = new Button();
            int position = j;
            button.setOnMouseReleased(e -> clicked(button, e, position));
            // button.setStyle(" -fx-focus-color: transparent; -fx-faint-focus-color:
            // transparent;");
            guessArea.getChildren().add(button);
            colourChangeButtons.add(button);
        }
    }

    public void clicked(Button button, MouseEvent mouseEvent, int position) {
        if (!locked && enabled) {
            setSelectedPosition(position);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (this.enabled) {
            getStyleClass().add("enabled");
        } else {
            getStyleClass().remove("enabled");
        }
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void colourPicked(Colour colour) {
        if (enabled && !locked) {
            String currentColour = guess.get(selectedPosition).toString();
            colourChangeButtons.get(selectedPosition).getStyleClass().remove(currentColour);
            boolean allColoursSetAlready = areAllColoursSet();
            guess.set(selectedPosition, colour);
            colourChangeButtons.get(selectedPosition).getStyleClass().add(colour.toString());
            logger.info("Button " + selectedPosition + " set to " + colour);
            incrementSelectedPosition();
            if (!allColoursSetAlready && areAllColoursSet()) {
                enableConfirmButton();
            }
        }
    }
    private void enableConfirmButton() {
        var confirmGuessButton = new Button("Ok!"); 
        getChildren().add(confirmGuessButton);
        confirmGuessButton.setOnMouseReleased(e -> {notifyGuessConfirmedListeners(); disableConfirmButton(confirmGuessButton);});
    }

    public void disableConfirmButton(Button button) {
        getChildren().remove(button);
    }
    public void addGuessConfirmedListener(GuessConfirmedListener guessConfirmedListener) {
        guessConfirmedListeners.add(guessConfirmedListener);
    }
    public void notifyGuessConfirmedListeners() {
        if (!locked && enabled) {
            logger.info("Guess made: " + guess);
            for (GuessConfirmedListener guessConfirmedListener : guessConfirmedListeners) {
                guessConfirmedListener.guessConfirmed(guess);
            }
        }
    }
    private boolean areAllColoursSet() {
        boolean allColoursSet = true;
        for (Colour colour : guess) {
            if (colour == Colour.NONE) {
                allColoursSet = false;
            }
        }
        return allColoursSet;
    }

    public void setSelectedPosition(int position) {
        colourChangeButtons.get(selectedPosition).getStyleClass().remove("selected");
        colourChangeButtons.get(position).getStyleClass().add("selected");
        selectedPosition = position;

    }
    public void incrementSelectedPosition() {
        setSelectedPosition((selectedPosition + 1) % App.PINS_PER_GUESS);
    }

    public void setFeedback(List<Colour> feedback) {
        List<Button> feedbackButtons = new ArrayList<>();
        for (Colour colour : feedback) {
            Button button = new Button();
            button.getStyleClass().addAll(colour.toString(), "result");
            feedbackButtons.add(button);
        }
        VBox resultColumn1 = new VBox();
        resultColumn1.getChildren().add(feedbackButtons.get(0));
        resultColumn1.getChildren().add(feedbackButtons.get(1));
        VBox resultColumn2 = new VBox();
        resultColumn2.getChildren().add(feedbackButtons.get(2));
        resultColumn2.getChildren().add(feedbackButtons.get(3));
        getChildren().addAll(resultColumn1, resultColumn2);
    }
}
