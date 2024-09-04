package me.thomasstokes.scene;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import me.thomasstokes.ui.GameWindow;
import me.thomasstokes.App;
import me.thomasstokes.component.ColourPicker;
import me.thomasstokes.component.GuessRow;
import me.thomasstokes.enums.Colour;
import me.thomasstokes.game.Game;
import me.thomasstokes.ui.GamePane;
public class Play extends Base {
    private static final Logger logger = LogManager.getLogger(Play.class);
    private final List<GuessRow> guessRows;
    private GridPane gridPane;
    private VBox guessesZone;
    private final Game game;
    private static Colour colour;
    public Play(GameWindow stage) {
        guessRows = new ArrayList<>();
        colour = Colour.NONE;
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
        for (int i = 0; i < App.MAX_GUESSES; i++) {
            var guessRow = new GuessRow();
            guessRows.add(guessRow);
            guessesZone.getChildren().add(guessRow);
        }
        var colourPicker = new ColourPicker();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(guessesZone, 1, 1);
        gridPane.add(colourPicker,2,1);
        guessRows.get(0).setEnabled(true);
    }

    public void initialise() {
        logger.info("widths of scene:" + scene.getWidth()+ " " + scene.getHeight());
    }

    public void guessMade() {
        game.guess(null);
    }
}
