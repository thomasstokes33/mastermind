package me.thomasstokes;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import me.thomasstokes.ui.GameWindow;

/**
 * JavaFX App
 */
public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class);
    public static final int PINS_PER_GUESS = 4;
    public static final int MAX_GUESSES = 10;
    private Stage stage;
    private double baseWidth = 500;
    private double baseHeight = 700;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        var gameWindow = new GameWindow(stage, baseWidth, baseHeight); // Needed so the object is still referenced and not garbage collected.
        this.stage.show();
        this.stage.setTitle("Mastermind");
        this.stage.setOnCloseRequest(event -> this.shutdown());
    }

    public static void main(String[] args) {
        launch();
    }

    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }

}