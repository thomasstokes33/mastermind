package me.thomasstokes.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.thomasstokes.App;
import me.thomasstokes.component.AnswerRevealedRow;
import me.thomasstokes.component.ColourPicker;
import me.thomasstokes.component.GuessRow;
import me.thomasstokes.enums.Colour;
import me.thomasstokes.game.Game;
import me.thomasstokes.game.GuessResultAndFeedback;
import me.thomasstokes.ui.GamePane;
import me.thomasstokes.ui.GameWindow;
public class Play extends Base {
    private static final Logger logger = LogManager.getLogger(Play.class);
    /**
     * This variable stores all the GuessRows for easy access.
     */
    private final List<GuessRow> guessRows;
    private GridPane gridPane;
    private VBox guessesZone;
    private final Game game;
    private ColourPicker colourPicker;
    private AnswerRevealedRow victoryBox;
    /**
     * This timer is used at the end of the game to delay an alert to the user.
     */
    private TimerTask timerTask;
    public Play(GameWindow stage) {
        guessRows = new ArrayList<>();
        window = stage;
        game = new Game();
        game.addGameOverListener((isVictory, answer) -> displayAnswer(isVictory, answer));
        logger.info("creating play scene");
    }

    public void build() {
        root = new StackPane();
        root.getStyleClass().add("backdrop");
        GamePane gamepane = new GamePane(window.getWidth(), window.getHeight());
        root.getChildren().add(gamepane);
        gridPane = new GridPane();
        gridPane.getStyleClass().add("play-background");
        root.getChildren().add(gridPane);
        gridPane.setMaxHeight(window.getHeight());
        ColumnConstraints columnConstraints0 = new ColumnConstraints(); // Constraints for column 0
        ColumnConstraints columnConstraints1 = new ColumnConstraints(); // Constraints for column 1 
        columnConstraints1.setMinWidth(280);
        columnConstraints1.setHgrow(Priority.ALWAYS); // Central column is responsive and grows.
        gridPane.getColumnConstraints().addAll(columnConstraints0, columnConstraints1);
        // setup rows - 0th guess row is first guess.
        guessesZone = new VBox();
        guessesZone.getStyleClass().add("guesses-zone");
        for (int i = 0; i < App.MAX_GUESSES; i++) {
            var guessRow = new GuessRow();
            guessRows.add(guessRow);
            guessesZone.getChildren().add(guessRow);
        }
        colourPicker = new ColourPicker();
        // Add the main components to the gridpane.
        gridPane.add(guessesZone, 1, 1);
        gridPane.add(colourPicker,2,1);
        guessRows.get(0).setEnabled(true); // Enable the first guess row.
        victoryBox = new AnswerRevealedRow();
        guessesZone.getChildren().add(victoryBox); // Add the row which is hiding the answer.
        setupNextColourPickedListener(0);
        setupNextGuessConfirmedListener(0);
        // Setup in built restart button
        var restartButton = new Button("Restart");
        restartButton.setOnMouseReleased(e -> displayRestartConfirmation());
        restartButton.getStyleClass().add("control");
        gridPane.add(restartButton, 2, 0);
    }

    public void setupNextColourPickedListener(int index) {
        colourPicker.addColourPickedListener((colour) -> {guessRows.get(index).colourPicked(colour);});
        //TODO: Clearup the last listener.
    }

    public void setupNextGuessConfirmedListener(int index) {
        guessRows.get(index).addGuessConfirmedListener(guess -> guessMade(guess));
    }

    public void initialise() {
        logger.info("Initialise Play scene");
        logger.info("Dimensions of scene: " + scene.getWidth()+ " x " + scene.getHeight());

    }
    /**
     * Disables and locks the previous row, enables the next row and also passes the guess to the Game backend.
     * The backend returns the feedback, which is then displayed.
     * @param guess The user's guess.
     */
    public void guessMade(List<Colour> guess) {
            GuessResultAndFeedback guessResultAndFeedback = game.guess(guess);  
            int numberOfGuesses = game.getNumOfGuesses();
            guessRows.get(numberOfGuesses - 1).setEnabled(false);
            guessRows.get(numberOfGuesses - 1).setFeedback(guessResultAndFeedback.getFeedback());
            guessRows.get(numberOfGuesses - 1).setLocked(true);
            if (!guessResultAndFeedback.isGameOver()) {
                logger.info("No end condition. Game CONTINUES");
                guessRows.get(numberOfGuesses).setEnabled(true);
                setupNextColourPickedListener(numberOfGuesses);
                setupNextGuessConfirmedListener(numberOfGuesses);
            }
    }

    /**
     * Reveals the hidden guess and displays the game outcome.
     * @param victory The game outcome: true for victory and false for a loss.
     * @param answer The correct answer.
     */
    public void displayAnswer(boolean victory, List<Colour> answer)  {
        logger.info("Game ended: displaying correct guess and waiting a few seconds...");
        String labelMessage;
        if (victory) {
            labelMessage = "Correct!";
        } else {
            labelMessage = "Incorrect :(";
        }
        guessesZone.getChildren().add(new Label(labelMessage + " I will give you a short moment to reflect..."));
        victoryBox.populateWithGuess(answer, true);
        Timer delay = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Runnable executeAfterExpiry;
                if (victory) {
                    executeAfterExpiry = () -> victory();
                } else {
                    executeAfterExpiry = () -> sadDefeat();
                }
                Platform.runLater(executeAfterExpiry);
            }
            
        };
        delay.schedule(timerTask, 5000);
    }
    /**
     * Clears up the game after correctly guessing the hidden pins.
     */
    public void victory() {
        logger.info("Victory");
        displayResultAlert("Correct Guess", "You win!");
        //TODO: Rework to use stack pane?
        window.cleanup();
        window.setPlayScene();
    }

    /**
     * Clears up the game after a loss.
     */
    public void sadDefeat() {
        logger.info("Loss");
        displayResultAlert("Out of guesses", "Better luck (and logic) next time :)");
        window.cleanup();
        window.setPlayScene();
    }

    /**
     * Displays an alert filled with the header and context text provided.
     */
    public void displayResultAlert(String headerText, String contentText) {
        var alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText + " Pressing ok will reset the game.");
        alert.showAndWait();
    }

    public void displayRestartConfirmation() {
        logger.info("Restarting game");
        Alert restartConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        restartConfirm.setHeaderText("Restart");
        restartConfirm.setContentText("Are you sure you want to restart?");
        Optional<ButtonType> result = restartConfirm.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals(ButtonType.OK)) {
                if (timerTask != null) {
                    timerTask.cancel(); // clears the alert that displays the result if the user clicks restart.
                }
                window.cleanup();
                window.setPlayScene();
            }
        }
        

    }

}
