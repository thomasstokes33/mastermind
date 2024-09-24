package me.thomasstokes.scene;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import me.thomasstokes.App;
import me.thomasstokes.component.ColourPicker;
import me.thomasstokes.component.GuessRow;
import me.thomasstokes.enums.Colour;
import me.thomasstokes.game.Game;
import me.thomasstokes.ui.GamePane;
import me.thomasstokes.ui.GameWindow;
public class Play extends Base {
    private static final Logger logger = LogManager.getLogger(Play.class);
    private final List<GuessRow> guessRows;
    private GridPane gridPane;
    private VBox guessesZone;
    private final Game game;
    private ColourPicker colourPicker;
    public Play(GameWindow stage) {
        guessRows = new ArrayList<>();
        window = stage;
        game = new Game();
        logger.info("creating play scene");
    }

    public void build() {
        root = new GamePane(window.getWidth(), window.getHeight());
        gridPane = new GridPane();
        gridPane.getStyleClass().add("play-background");
        root.getChildren().add(gridPane);
        gridPane.setMaxWidth(window.getWidth());
        gridPane.setMaxHeight(window.getHeight());
        // setup rows
        guessesZone = new VBox(10);
        ColumnConstraints columnConstraints0 = new ColumnConstraints();
        ColumnConstraints columnConstraints1 = new ColumnConstraints(300, 300, 300);
        columnConstraints1.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnConstraints0, columnConstraints1);
        gridPane.setGridLinesVisible(true);
        for (int i = 0; i < App.MAX_GUESSES; i++) {
            var guessRow = new GuessRow();
            guessRows.add(guessRow);
            guessesZone.getChildren().add(guessRow);
        }
        colourPicker = new ColourPicker();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(guessesZone, 1, 1);
        gridPane.add(colourPicker,2,1);
        guessRows.get(0).setEnabled(true);
    }

    public void initialise() {
        logger.info("widths of scene:" + scene.getWidth()+ " " + scene.getHeight());
    }

    public void guessMade(List<Colour> guess) {
        if (game.guess(guess)) {
            victory();
        } else {
            int numberOfGuesses = game.getNumOfGuesses();
            guessRows.get(numberOfGuesses - 1).setEnabled(false);
            guessRows.get(numberOfGuesses - 1).setLocked(true);
            guessRows.get(numberOfGuesses).setEnabled(true);
        }
    }

    public void victory() {
        var victoryHBox = new HBox();
        guessesZone.getChildren().add(new Label("victory"));
        //TODO: Display victory setup and message
        window.cleanup();
        window.setPlayScene();
    }

}
