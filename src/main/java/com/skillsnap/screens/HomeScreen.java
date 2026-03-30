package com.skillsnap.screens;
import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.GameDAO;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import com.skillsnap.utils.AnimationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class HomeScreen {

    private GameDAO gameDAO = new GameDAO();

    public Pane getLayout() {
        Player player = PlayerSession.getInstance().getCurrentPlayer();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── NAV BAR ───────────────────────────────────────────────────
        HBox navbar = new HBox();
        navbar.getStyleClass().add("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPrefHeight(60);
        navbar.setSpacing(8);
        navbar.setPadding(new Insets(0, 20, 0, 20));

        Text logo = new Text("SkillSnap");
        logo.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: #2E75B6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Nav buttons
        Button careersBtn  = makeNavBtn("Careers");
        Button profileBtn  = makeNavBtn("Profile");
        Button leaderBtn   = makeNavBtn("Leaderboard");
        Button logoutBtn   = makeNavBtn("Logout");
        logoutBtn.setStyle(logoutBtn.getStyle() +
                "-fx-text-fill: #F85149;");

        careersBtn.setOnAction(e ->
                ScreenManager.getInstance().showCareerMap());
        profileBtn.setOnAction(e ->
                ScreenManager.getInstance().showProfile());
        leaderBtn.setOnAction(e ->
                ScreenManager.getInstance().showLeaderboard());
        logoutBtn.setOnAction(e -> {
            PlayerSession.getInstance().logout();
            ScreenManager.getInstance().showWelcome();
        });

        navbar.getChildren().addAll(
                logo, spacer,
                careersBtn, profileBtn, leaderBtn, logoutBtn);
        root.setTop(navbar);
        // ── MAIN CONTENT ──────────────────────────────────────
        VBox content = new VBox(32);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40));

        // Welcome message
        Text welcome = new Text("Welcome back, " +
                player.getFullName().split(" ")[0] + "!");
        welcome.getStyleClass().add("heading-text");

        // ── STATS ROW ─────────────────────────────────────────
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);

        VBox xpCard   = makeStatCard("Total XP",
                String.valueOf(player.getTotalXP()));
        VBox lvlCard  = makeStatCard("Level",
                String.valueOf(player.getLevel()));
        VBox streakCard = makeStatCard("Streak",
                player.getStreak() + " days");
        VBox gamesCard  = makeStatCard("Games Played",
                String.valueOf(
                        gameDAO.getTotalGamesPlayed(player.getPlayerId())));

        statsRow.getChildren().addAll(
                xpCard, lvlCard, streakCard, gamesCard);

        // ── XP PROGRESS BAR ───────────────────────────────────
        VBox xpSection = new VBox(8);
        xpSection.setMaxWidth(500);
        int xpForNext = (player.getLevel() * 100);
        int currentXP = player.getTotalXP() % 100;
        Text xpLabel = new Text("XP to next level: " +
                currentXP + " / " + xpForNext);
        xpLabel.getStyleClass().add("body-text");
        ProgressBar xpBar = new ProgressBar(currentXP * 1.0 / xpForNext);
        xpBar.getStyleClass().add("xp-bar");
        xpBar.setPrefWidth(500);
        xpBar.setPrefHeight(10);
        xpSection.getChildren().addAll(xpLabel, xpBar);
        xpSection.setAlignment(Pos.CENTER);

        // ── START PLAYING BUTTON ──────────────────────────────
        Button playBtn = new Button("Explore Careers");
        Button suggBtn = new Button("My Career Match");
        suggBtn.getStyleClass().add("btn-secondary");
        suggBtn.setPrefWidth(220);
        suggBtn.setPrefHeight(50);
        suggBtn.setOnAction(e ->
                ScreenManager.getInstance().showSuggestion());
        playBtn.getStyleClass().add("btn-primary");
        playBtn.setPrefWidth(220);
        playBtn.setPrefHeight(50);
        playBtn.setStyle(playBtn.getStyle() +
                "-fx-font-size: 16px;");
        playBtn.setOnAction(e ->
                ScreenManager.getInstance().showCareerMap());

        content.getChildren().addAll(
                welcome, statsRow, xpSection, playBtn, suggBtn);
        root.setCenter(content);
        AnimationUtils.slideInFromRight(content);
        return root;


    }

    // ── Stat card helper ──────────────────────────────────────
    private VBox makeStatCard(String label, String value) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("card");
        card.setPrefWidth(160);
        card.setPrefHeight(100);

        Text valueText = new Text(value);
        valueText.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: #2E75B6;");

        Text labelText = new Text(label);
        labelText.getStyleClass().add("subtitle-text");

        card.getChildren().addAll(valueText, labelText);
        return card;
    }
    private Button makeNavBtn(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #C9D1D9;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 8 16;");
        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-color: #1C2128;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-cursor: hand;" +
                                "-fx-border-color: transparent;" +
                                "-fx-background-radius: 6;" +
                                "-fx-padding: 8 16;"));
        btn.setOnMouseExited(e ->
                btn.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #C9D1D9;" +
                                "-fx-font-size: 14px;" +
                                "-fx-cursor: hand;" +
                                "-fx-border-color: transparent;" +
                                "-fx-padding: 8 16;"));
        return btn;
    }
}
