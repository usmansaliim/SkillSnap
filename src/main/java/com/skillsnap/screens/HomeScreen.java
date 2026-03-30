package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.GameDAO;
import com.skillsnap.database.QuestDAO;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import com.skillsnap.models.quest.Quest;
import com.skillsnap.utils.AnimationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.List;
import com.skillsnap.utils.SoundEngine;

public class HomeScreen {

    private GameDAO gameDAO   = new GameDAO();
    private QuestDAO questDAO = new QuestDAO();

    public Pane getLayout() {
        Player player = PlayerSession.getInstance().getCurrentPlayer();

        // Assign today's quests if not already done
        questDAO.assignDailyQuestsIfNeeded(player.getPlayerId());

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── NAV BAR ──────────────────────────────────────────
        HBox navbar = new HBox();
        navbar.getStyleClass().add("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPrefHeight(60);
        navbar.setSpacing(8);
        navbar.setPadding(new Insets(0, 20, 0, 20));

        Text logo = new Text("SkillSnap");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;" +
                "-fx-fill: #2E75B6;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button careersBtn = makeNavBtn("Careers");
        Button friendsBtn = makeNavBtn("Friends");
        friendsBtn.setOnAction(e ->
                ScreenManager.getInstance().showFriends());
        Button profileBtn = makeNavBtn("Profile");
        Button leaderBtn  = makeNavBtn("Leaderboard");
        Button logoutBtn  = makeNavBtn("Logout");
        logoutBtn.setStyle(logoutBtn.getStyle() + "-fx-text-fill: #F85149;");

        careersBtn.setOnAction(e -> ScreenManager.getInstance().showCareerMap());
        profileBtn.setOnAction(e -> ScreenManager.getInstance().showProfile());
        leaderBtn.setOnAction(e  -> ScreenManager.getInstance().showLeaderboard());
        logoutBtn.setOnAction(e  -> {
            PlayerSession.getInstance().logout();
            ScreenManager.getInstance().showWelcome();
        });

        navbar.getChildren().addAll(logo, spacer,
                careersBtn, friendsBtn, profileBtn, leaderBtn, logoutBtn);
        root.setTop(navbar);

        // ── SCROLLABLE MAIN CONTENT ───────────────────────────
        VBox content = new VBox(28);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(36, 40, 40, 40));

        // Welcome message
        Text welcome = new Text("Welcome back, " +
                player.getFullName().split(" ")[0] + "!");
        welcome.getStyleClass().add("heading-text");

        // ── STATS ROW ─────────────────────────────────────────
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
                makeStatCard("Total XP",
                        String.valueOf(player.getTotalXP())),
                makeStatCard("Level",
                        String.valueOf(player.getLevel())),
                makeStatCard("Streak",
                        player.getStreak() + " days"),
                makeStatCard("Games Played",
                        String.valueOf(gameDAO.getTotalGamesPlayed(
                                player.getPlayerId())))
        );

        // ── XP PROGRESS BAR ───────────────────────────────────
        VBox xpSection = new VBox(8);
        xpSection.setMaxWidth(500);
        xpSection.setAlignment(Pos.CENTER);
        int currentXP = player.getTotalXP() % 100;
        int xpForNext = 100;
        Text xpLabel = new Text("XP to next level: " +
                currentXP + " / " + xpForNext);
        xpLabel.getStyleClass().add("body-text");
        ProgressBar xpBar = new ProgressBar(currentXP * 1.0 / xpForNext);
        xpBar.getStyleClass().add("xp-bar");
        xpBar.setPrefWidth(500);
        xpSection.getChildren().addAll(xpLabel, xpBar);

        // ── DAILY QUESTS PANEL ────────────────────────────────
        VBox questsPanel = buildQuestsPanel(player.getPlayerId());
        questsPanel.setMaxWidth(600);

        // ── ACTION BUTTONS ────────────────────────────────────
        HBox btnRow = new HBox(16);
        btnRow.setAlignment(Pos.CENTER);

        Button playBtn = new Button("Explore Careers");
        playBtn.getStyleClass().add("btn-primary");
        playBtn.setPrefWidth(220);
        playBtn.setPrefHeight(50);
        playBtn.setStyle(playBtn.getStyle() + "-fx-font-size: 16px;");
        playBtn.setOnAction(e -> {
            SoundEngine.getInstance().playClick();
            ScreenManager.getInstance().showCareerMap();
        });

        Button suggBtn = new Button("My Career Match");
        suggBtn.getStyleClass().add("btn-secondary");
        suggBtn.setPrefWidth(220);
        suggBtn.setPrefHeight(50);
        suggBtn.setOnAction(e -> {
            SoundEngine.getInstance().playClick();
            ScreenManager.getInstance().showSuggestion();
        });

        btnRow.getChildren().addAll(playBtn, suggBtn);

        content.getChildren().addAll(
                welcome, statsRow, xpSection, questsPanel, btnRow);

        // Wrap in ScrollPane for smaller screens
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;" +
                "-fx-background: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.setCenter(scroll);
        AnimationUtils.slideInFromRight(content);
        return root;
    }

    // ── Build daily quests panel ──────────────────────────────
    private VBox buildQuestsPanel(int playerId) {
        VBox panel = new VBox(12);
        panel.setAlignment(Pos.TOP_LEFT);

        // Header row
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Text title = new Text("Daily Quests");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;" +
                "-fx-fill: #FFFFFF;");
        Text subtitle = new Text("Resets at midnight");
        subtitle.setStyle("-fx-font-size: 12px; -fx-fill: #8B949E;");
        header.getChildren().addAll(title, subtitle);

        panel.getChildren().add(header);

        List<Quest> quests = questDAO.getDailyQuestsForPlayer(playerId);

        if (quests.isEmpty()) {
            Text none = new Text("No daily quests available.");
            none.getStyleClass().add("body-text");
            panel.getChildren().add(none);
            return panel;
        }

        for (Quest q : quests) {
            panel.getChildren().add(makeQuestCard(q));
        }

        return panel;
    }

    // ── Single quest card ─────────────────────────────────────
    private VBox makeQuestCard(Quest q) {
        VBox card = new VBox(8);
        card.getStyleClass().add(
                q.isCompleted() ? "quest-card-completed" : "quest-card");

        // Top row — title + XP reward
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        Text title = new Text(
                (q.isCompleted() ? "✅  " : "⬜  ") + q.getTitle());
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;" +
                "-fx-fill: " + (q.isCompleted() ?
                "#3FB950" : "#C9D1D9") + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text xpText = new Text("+" + q.getXpReward() + " XP");
        xpText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;" +
                "-fx-fill: #2E75B6;");

        topRow.getChildren().addAll(title, spacer, xpText);

        // Description
        Text desc = new Text(q.getDescription());
        desc.setStyle("-fx-font-size: 12px; -fx-fill: #8B949E;");

        // Progress bar + counter
        HBox progressRow = new HBox(10);
        progressRow.setAlignment(Pos.CENTER_LEFT);

        int progress = Math.min(q.getProgress(), q.getConditionValue());
        double pct = q.getConditionValue() > 0
                ? (double) progress / q.getConditionValue() : 0;

        ProgressBar bar = new ProgressBar(q.isCompleted() ? 1.0 : pct);
        bar.getStyleClass().add(
                q.isCompleted() ? "quest-bar-done" : "quest-bar");
        bar.setPrefWidth(380);

        Text counter = new Text(progress + " / " + q.getConditionValue());
        counter.setStyle("-fx-font-size: 12px; -fx-fill: #8B949E;");

        progressRow.getChildren().addAll(bar, counter);
        card.getChildren().addAll(topRow, desc, progressRow);
        return card;
    }

    // ── Stat card helper ──────────────────────────────────────
    private VBox makeStatCard(String label, String value) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("card");
        card.setPrefWidth(160);
        card.setPrefHeight(100);

        Text valueText = new Text(value);
        valueText.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;" +
                "-fx-fill: #2E75B6;");
        Text labelText = new Text(label);
        labelText.getStyleClass().add("subtitle-text");

        card.getChildren().addAll(valueText, labelText);
        return card;
    }

    // ── Nav button helper ─────────────────────────────────────
    private Button makeNavBtn(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #C9D1D9;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 8 16;");
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #1C2128;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 8 16;"));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #C9D1D9;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 8 16;"));
        return btn;
    }
}