package me.thomasstokes;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import me.thomasstokes.ui.GameWindow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * JavaFX App
 */
public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class);
    private Stage stage;
    private int baseWidth = 500;
    private int baseHeight = 600;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        var gameWindow = new GameWindow(stage, baseWidth, baseHeight);
        stage.show();
        stage.setTitle("Mastermind");
        stage.setOnCloseRequest(event -> this.shutdown());

    }

    public static void main(String[] args) {
        launch();
    }

    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }

}