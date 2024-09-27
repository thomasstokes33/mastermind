package me.thomasstokes.component;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import me.thomasstokes.enums.Colour;
import me.thomasstokes.listeners.ColourPickedListener;
/**
 * This is the component that allows users to pick a colour for a given pin. It notifies any listeners when a colour is picked.
 */
public class ColourPicker extends VBox {
    private final List<ColourPickedListener> colourChangedListeners;
    private static final Logger logger = LogManager.getLogger(ColourPicker.class);
    public ColourPicker() {
        colourChangedListeners = new ArrayList<>();
        getStyleClass().add("colour-picker");
        build();
    }
    public void build() {
        getChildren().clear();
        for (int i = 0; i < Colour.VALUES_WITHOUT_NONE.length; i++) {
            var button = new Button();
            getChildren().add(button);
            // button.setStyle("-fx-background-color: " + Colour.VALUES[i].toString().toLowerCase());
            Colour colour = Colour.VALUES_WITHOUT_NONE[i];
            button.getStyleClass().add(colour.toString().toLowerCase());
            button.setOnMouseReleased(e -> colourChosen(colour));
        }
    }
    public void colourChosen(Colour colourPicked) {
        logger.info("Color picked:" + colourPicked);
        notifyColourPickedListeners(colourPicked);
    }

    public void notifyColourPickedListeners(Colour colourPicked) {
        for (ColourPickedListener colourPickedListener : colourChangedListeners) {
            colourPickedListener.colourPicked(colourPicked);
        }
    }

    public ColourPickedListener addColourPickedListener(ColourPickedListener colourPickedListener) {
        colourChangedListeners.add(colourPickedListener);
        return colourPickedListener;
    }
}
