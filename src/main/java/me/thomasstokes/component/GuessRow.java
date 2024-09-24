package me.thomasstokes.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
        guess = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            guess.add(Colour.NONE);
        }
        colourChangeButtons = new ArrayList<>();
        getStyleClass().add("guess-row");
        build();
        setSelectedPosition(0);
    }

    public void build() {
        getChildren().clear();

        guessArea = new HBox(10);
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

    public void clicked(Button button, MouseEvent e, int position) {
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
            logger.info(colourChangeButtons.get(selectedPosition).getStyleClass());
            incrementSelectedPosition();
            if (!allColoursSetAlready && areAllColoursSet()) {
                enableConfirmButton();
            }
        }
    }
    private void enableConfirmButton() {
        var confirmGuessButton = new Button("Ok!"); 
        guessArea.getChildren().add(confirmGuessButton);
        confirmGuessButton.setOnMouseReleased(e -> {notifyGuessConfirmedListeners(); disableConfirmButton(confirmGuessButton);});
    }

    public void disableConfirmButton(Button button) {
        guessArea.getChildren().remove(button);
    }
    public void addGuessConfirmedListener(GuessConfirmedListener guessConfirmedListener) {
        guessConfirmedListeners.add(guessConfirmedListener);
    }
    public void notifyGuessConfirmedListeners() {
        if (!locked && enabled) {
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
        setSelectedPosition((selectedPosition + 1) % 4);
    }
}
