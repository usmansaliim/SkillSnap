package com.skillsnap.app;

import com.skillsnap.models.career.CareerPath;
import com.skillsnap.models.game.MiniGame;
import com.skillsnap.models.game.GameResult;
import com.skillsnap.screens.WelcomeScreen;
import com.skillsnap.screens.LoginScreen;
import com.skillsnap.screens.RegisterScreen;
import com.skillsnap.screens.HomeScreen;
import com.skillsnap.screens.CareerMapScreen;
import com.skillsnap.screens.GameLobbyScreen;
import com.skillsnap.screens.GameScreen;
import com.skillsnap.screens.ResultsScreen;
import com.skillsnap.screens.RoadmapScreen;
import com.skillsnap.screens.ProfileScreen;
import com.skillsnap.screens.LeaderboardScreen;
import javafx.scene.Scene;
import com.skillsnap.screens.SuggestionScreen;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ScreenManager {

    private static ScreenManager instance;
    private Stage stage;

    // Window dimensions — change here to affect whole app
    public static final int WIDTH  = 1000;
    public static final int HEIGHT = 700;

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) instance = new ScreenManager();
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("SkillSnap");
        this.stage.setWidth(WIDTH);
        this.stage.setHeight(HEIGHT);
        this.stage.setResizable(false);
    }

    // ── NAVIGATION METHODS ────────────────────────────────────
    public void showWelcome() {
        setScene(new WelcomeScreen().getLayout());
    }

    public void showLogin() {
        setScene(new LoginScreen().getLayout());
    }

    public void showRegister() {
        setScene(new RegisterScreen().getLayout());
    }

    public void showHome() {
        setScene(new HomeScreen().getLayout());
    }

    public void showCareerMap() {
        setScene(new CareerMapScreen().getLayout());
    }

    public void showGameLobby(CareerPath career) {
        GameLobbyScreen screen = new GameLobbyScreen(career);
        setScene(screen.getLayout());
    }

    public void showGame(MiniGame game, CareerPath career) {
        setScene(new GameScreen(game, career).getLayout());
    }

    public void showResults(GameResult result, CareerPath career) {
        setScene(new ResultsScreen(result, career).getLayout());
    }
    public void showProfile() {
        setScene(new ProfileScreen().getLayout());
    }

    public void showSuggestion() {
        setScene(new SuggestionScreen().getLayout());
    }

    public void showBadges() {
        // Badges shown inside ProfileScreen for now
        setScene(new ProfileScreen().getLayout());
    }

    public void showLeaderboard() {
        setScene(new LeaderboardScreen().getLayout());
    }
    public void showRoadmap(CareerPath career) {
        setScene(new RoadmapScreen(career).getLayout());
    }

    // ── PRIVATE HELPER ────────────────────────────────────────
    private void setScene(javafx.scene.layout.Pane layout) {
        Scene scene = new Scene(layout, WIDTH, HEIGHT);

        // Load global stylesheet
        String css = getClass()
                .getResource("/css/main.css")
                .toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }


}
