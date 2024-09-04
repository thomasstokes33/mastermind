package me.thomasstokes.component;
import javafx.scene.control.Button;

import javafx.scene.layout.VBox;

import me.thomasstokes.enums.Colour;
public class ColourPicker extends VBox {
    public ColourPicker() {
        getStyleClass().add("picker");
        build();
    }
    public void build() {
        setSpacing(5);
        getChildren().clear();
        // -1 because one colour is none.

        for (int i = 0; i < Colour.VALUES.length - 1; i++) {
            var button = new Button();
            getChildren().add(button);
            // button.setStyle("-fx-background-color: " + Colour.VALUES[i].toString().toLowerCase());
            
            button.getStyleClass().add(Colour.VALUES[i].toString().toLowerCase());
        }
    }
}
