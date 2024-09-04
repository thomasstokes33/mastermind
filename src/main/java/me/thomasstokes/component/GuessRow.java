package me.thomasstokes.component;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import me.thomasstokes.App;
import me.thomasstokes.enums.Colour;


public class GuessRow extends HBox{

    private static final Logger logger = LogManager.getLogger(GuessRow.class);

    private HBox guessArea;
    private final ArrayList<Colour> guess; 
    private boolean locked = false;
    private boolean enabled = false;
    /** 
     * The selected position. Starts at 0.
     */
    private int selectedPosition = 0;
    public GuessRow() {
        guess = new ArrayList<>(4);
        getStyleClass().add("guess-row");
        build();
    }
    public void build() {
        getChildren().clear();

        guessArea = new HBox(10);

        getChildren().add(guessArea);
        for (int j = 0; j < App.PINS_PER_GUESS; j ++) {
            var button = new Button();
            button.setOnMouseReleased(e -> clicked(e));
            // button.setStyle(" -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
            guessArea.getChildren().add(button);
        }
    }
    public void clicked(MouseEvent e) {
        if (!locked) {
            
        } 
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            getStyleClass().add("enabled");
        } else {
            getStyleClass().remove("enabled");
        }
    }


}
