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
    /**
     * The listeners to notify when a guess is confirmed.
     */
    private List<GuessConfirmedListener> guessConfirmedListeners = new ArrayList<>();
    /**
     * This is a HBox nested within the GuessRow which contains the "pin" Button objects.
     */
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
    /**
     * If true it indicates that the GuessRow has submitted a guess and has feedback. It effectively inhibts editing of the GuessRow.
     */
    private boolean locked = false;
    /**
     * If true it indicates that the GuessRow is active and it means the user has reached this row.
     */
    private boolean enabled = false;
    /**
     * The selected pin position. Starts at indexing 0 on left.
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

    /**
     * The handler method for when a Button pin is clicked. 
     * @param button The Button clicked.
     * @param position The position of the Button clicked as a number. Counting starts from the left and from 0.
     */
    public void clicked(Button button, MouseEvent mouseEvent, int position) {
        if (!locked && enabled) {
            setSelectedPosition(position);
        }
    }

    /**
     * Set whether the Guess Row is {@link GuessRow#enabled enabled}.
     * @param enabled true to enable and false to disable.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (this.enabled) {
            getStyleClass().add("enabled");
        } else {
            getStyleClass().remove("enabled");
        }
    }
    /**
     * Sets if the GuessRow is {@link GuessRow#locked locked}.
     * @param locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * The handler for when a colour is picked. This will update the currently {@link GuessRow#selectedPosition selected position's} Button 
     * to the passed Colour.
     * @param colour The colour the user picked.
     */
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
    /**
     * Adds a confirm Button to the GuessRow which the user can click to submit their guess.
     */
    private void enableConfirmButton() {
        var confirmGuessButton = new Button("Ok!"); 
        getChildren().add(confirmGuessButton);
        confirmGuessButton.setOnMouseReleased(e -> {notifyGuessConfirmedListeners(); disableConfirmButton(confirmGuessButton);});
    }

    /**
     * Removes the passed Button from the GuessRow.
     * @param button The Button to remove.
     */
    public void disableConfirmButton(Button button) {
        getChildren().remove(button);
    }

    /**
     * 
     * @param guessConfirmedListener Listener to add to {@link GuessRow#guessConfirmedListeners}.
     */
    public void addGuessConfirmedListener(GuessConfirmedListener guessConfirmedListener) {
        guessConfirmedListeners.add(guessConfirmedListener);
    }
    /**
     * Handler for when a guess had been confirmed (meaning it can no longer be changed).
     */
    public void notifyGuessConfirmedListeners() {
        if (!locked && enabled) {
            logger.info("Guess made: " + guess);
            colourChangeButtons.get(selectedPosition).getStyleClass().remove("selected"); // No need to de-focus as focus is shifted to confirmation button.
            for (GuessConfirmedListener guessConfirmedListener : guessConfirmedListeners) {
                guessConfirmedListener.guessConfirmed(guess);
            }
        }
    }

    /**
     * Checks if the user has chosen a colour for each pin.
     * @return Returns true if all pins have a chosen colour.
     */
    private boolean areAllColoursSet() {
        boolean allColoursSet = true;
        for (Colour colour : guess) {
            if (colour == Colour.NONE) {
                allColoursSet = false;
            }
        }
        return allColoursSet;
    }

    /**
     * Sets the selected position, which will alter the highlighted position for the user.
     */
    public void setSelectedPosition(int position) {
        colourChangeButtons.get(selectedPosition).getStyleClass().remove("selected");
        colourChangeButtons.get(position).getStyleClass().add("selected");
        selectedPosition = position;
    }

    /**
     * Increments the stored selected position.
     */
    public void incrementSelectedPosition() {
        setSelectedPosition((selectedPosition + 1) % App.PINS_PER_GUESS);
    }

    /**
     * Adds the passed feedback to the GuessRow.
     * @param feedback This is a list of colours indicating how correct a guess is. The Colours must be {@link Colour#CORRECT_COLOUR_WRONG_POSITION_RESULT_COLOUR}
     * or {@link Colour#CORRECT_COLOUR_CORRECT_POSITION_RESULT_COLOUR}.
     */
    public void setFeedback(List<Colour> feedback) {
        if (!locked) {
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
}
