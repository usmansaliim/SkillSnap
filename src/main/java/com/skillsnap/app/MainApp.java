package com.skillsnap.app;

import com.skillsnap.database.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Connect to database
        DatabaseManager.getInstance();

        // Hand stage to ScreenManager
        ScreenManager.getInstance().setStage(primaryStage);

        // Show first screen
        ScreenManager.getInstance().showWelcome();
    }

    @Override
    public void stop() {
        // Disconnect DB cleanly when window closes
        DatabaseManager.getInstance().disconnect();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
