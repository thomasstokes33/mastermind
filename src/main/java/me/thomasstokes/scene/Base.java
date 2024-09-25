package me.thomasstokes.scene;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import me.thomasstokes.ui.GamePane;
import me.thomasstokes.ui.GameWindow;

public abstract class Base {
    protected Scene scene;
    protected GamePane root;
    protected GameWindow window;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public void build() {

    }

    public void initialise() {

    }

    public Scene createScene() {
        var previousScene = window.getScene();
        this.scene = new Scene(root, previousScene.getWidth(), previousScene.getHeight(), Color.WHITE);
        this.scene.getStylesheets().add(getClass().getResource("/style/game.css").toExternalForm());
        return scene;
    }

    public Scene getScene() {
        return scene;
    }

}