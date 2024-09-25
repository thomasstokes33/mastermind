package me.thomasstokes.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.thomasstokes.scene.Base;
import me.thomasstokes.scene.Play;

public class GameWindow {
    private static final Logger logger = LogManager.getLogger(GameWindow.class);

    private final double width;
    private final double height;
    private final Stage stage;
    private Scene scene;
    public GameWindow(Stage stage, double width, double height) {
        this.width = width;
        this.height = height;
        this.stage = stage;
        stage.setMinHeight(height);
        stage.setMinWidth(width);
        setupResources();
        setBlankScene();
        setPlayScene();
    }
    private void setupResources() {
        logger.info("Setting up resources");
        //We need to load fonts here due to the Font loader bug with spaces in URLs in the CSS files
        // Font.loadFont(getClass().getResourceAsStream("/style/Orbitron-Regular.ttf"), 32);
    }
    private void setBlankScene() {
        logger.info("Setting blank scene");
        this.scene = new Scene(new Pane(), width, height, Color.BLACK);
        stage.setScene(this.scene);
    }
    public void setPlayScene() {
        loadScene(new Play(this));
    }
    public void loadScene(Base scene) {
        logger.info("Loading scene: " + scene.toString());
        cleanup();
        scene.build();
        this.scene = scene.createScene();
        stage.setScene(this.scene);
        Platform.runLater(() -> scene.initialise());
    }

    public void cleanup() {
        logger.info("Cleaning up stage");

    }

    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }
}