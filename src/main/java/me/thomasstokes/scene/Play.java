package me.thomasstokes.scene;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import me.thomasstokes.ui.GameWindow;
import me.thomasstokes.game.Game;
import me.thomasstokes.ui.GamePane;
public class Play extends Base {
    private static final Logger logger = LogManager.getLogger(Play.class);
    // An arraylist storing all the rows.
    private ArrayList<HBox> rows;
    private GridPane gridPane;
    private VBox playZone;
    private Game game;
    public Play(GameWindow stage) {
        window = stage;
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
        playZone = new VBox(10);
        rows = new ArrayList<HBox>();
        for (int i = 0; i < 10; i++) {
            var box = new HBox(10);
            rows.add(box);
            playZone.getChildren().add(box);
            for (int j = 0; j < 4; j ++) {
                box.getChildren().add(new Circle(20));
            }
        }
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(playZone, 1, 1);
    }

    public void initialise() {
        logger.info("widths of scene:" + scene.getWidth()+ " " + scene.getHeight());
    }
}
