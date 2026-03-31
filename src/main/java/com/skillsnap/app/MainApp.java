package com.skillsnap.app;

import com.skillsnap.database.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // ✅ Actually connect to database
        DatabaseManager.getInstance();

        ScreenManager.getInstance().setStage(primaryStage);
        ScreenManager.getInstance().showWelcome();
    }

    @Override
    public void stop() {
        DatabaseManager.getInstance().disconnect();
    }

    public static void main(String[] args) {
        launch(args);
    }
}