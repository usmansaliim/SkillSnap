package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.GameDAO;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ProfileScreen {

    private GameDAO gameDAO = new GameDAO();

    public Pane getLayout() {
        Player player =
                PlayerSession.getInstance().getCurrentPlayer();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── NAV ───────────────────────────────────────────────
        HBox navbar = new HBox();
        navbar.getStyleClass().add("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPrefHeight(60);
        navbar.setSpacing(16);
        navbar.setPadding(new Insets(0, 30, 0, 30));

        Button backBtn = new Button("← Home");
        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2E75B6; -fx-cursor: hand;" +
                        "-fx-border-color: transparent; -fx-font-size: 14px;");
        backBtn.setOnAction(e ->
                ScreenManager.getInstance().showHome());

        Text navTitle = new Text("My Profile");
        navTitle.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        navbar.getChildren().addAll(backBtn, navTitle);
        root.setTop(navbar);

        // ── CONTENT ───────────────────────────────────────────
        VBox content = new VBox(28);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(36));

        // ── AVATAR + NAME CARD ────────────────────────────────
        VBox profileCard = new VBox(16);
        profileCard.setAlignment(Pos.CENTER);
        profileCard.getStyleClass().add("card");
        profileCard.setMaxWidth(600);
        profileCard.setPadding(new Insets(30));

        // Avatar circle
        VBox avatarCircle = new VBox();
        avatarCircle.setAlignment(Pos.CENTER);
        avatarCircle.setStyle(
                "-fx-background-color: #1F3864;" +
                        "-fx-background-radius: 50;" +
                        "-fx-border-color: #2E75B6;" +
                        "-fx-border-radius: 50;" +
                        "-fx-border-width: 3;" +
                        "-fx-min-width: 100px; -fx-min-height: 100px;" +
                        "-fx-max-width: 100px; -fx-max-height: 100px;");

        Text avatarText = new Text(
                String.valueOf(player.getFullName()
                        .charAt(0)).toUpperCase());
        avatarText.setStyle("-fx-font-size: 40px; " +
                "-fx-font-weight: bold; -fx-fill: #2E75B6;");
        avatarCircle.getChildren().add(avatarText);

        Text nameText = new Text(player.getFullName());
        nameText.setStyle("-fx-font-size: 24px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        Text usernameText = new Text("@" + player.getUsername());
        usernameText.setStyle(
                "-fx-font-size: 15px; -fx-fill: #8B949E;");

        Text uniText = new Text(
                player.getUniversity() != null &&
                        !player.getUniversity().isEmpty() ?
                        player.getUniversity() : "University not set");
        uniText.setStyle(
                "-fx-font-size: 14px; -fx-fill: #2E75B6;");

        profileCard.getChildren().addAll(
                avatarCircle, nameText, usernameText, uniText);

        // ── STATS GRID ────────────────────────────────────────
        Text statsTitle = new Text("Statistics");
        statsTitle.setStyle("-fx-font-size: 20px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        int totalGames =
                gameDAO.getTotalGamesPlayed(player.getPlayerId());
        int careersTried =
                gameDAO.getDistinctCareersTried(player.getPlayerId());

        HBox statsRow = new HBox(16);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
                makeStatCard("Level",
                        String.valueOf(player.getLevel()), "#2E75B6"),
                makeStatCard("Total XP",
                        String.valueOf(player.getTotalXP()), "#E3B341"),
                makeStatCard("Games Played",
                        String.valueOf(totalGames), "#3FB950"),
                makeStatCard("Careers Tried",
                        String.valueOf(careersTried), "#F78166"),
                makeStatCard("Streak",
                        player.getStreak() + " days", "#BC8CFF")
        );

        // ── XP PROGRESS ───────────────────────────────────────
        VBox xpSection = new VBox(10);
        xpSection.setMaxWidth(600);
        xpSection.setAlignment(Pos.CENTER_LEFT);

        int currentXP  = player.getTotalXP() % 100;
        int nextLevelXP = 100;
        double progress = currentXP / 100.0;

        Text xpTitle = new Text("Progress to Level " +
                (player.getLevel() + 1));
        xpTitle.setStyle("-fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        ProgressBar xpBar = new ProgressBar(progress);
        xpBar.setPrefWidth(600);
        xpBar.setPrefHeight(14);
        xpBar.getStyleClass().add("xp-bar");

        Text xpLabel = new Text(currentXP +
                " / " + nextLevelXP + " XP");
        xpLabel.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");

        xpSection.getChildren().addAll(
                xpTitle, xpBar, xpLabel);

        // ── BADGES PREVIEW ────────────────────────────────────
        Text badgesTitle = new Text("Badges");
        badgesTitle.setStyle("-fx-font-size: 20px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        HBox badgesRow = new HBox(12);
        badgesRow.setAlignment(Pos.CENTER_LEFT);

        // Placeholder badges based on stats
        if (totalGames >= 1)
            badgesRow.getChildren().add(
                    makeBadge("First Step", "#2E75B6"));
        if (player.getStreak() >= 3)
            badgesRow.getChildren().add(
                    makeBadge("On Fire", "#F85149"));
        if (totalGames >= 25)
            badgesRow.getChildren().add(
                    makeBadge("Grinder", "#E3B341"));
        if (careersTried >= 3)
            badgesRow.getChildren().add(
                    makeBadge("Explorer", "#3FB950"));
        if (badgesRow.getChildren().isEmpty()) {
            Text noBadges = new Text(
                    "Play games to earn badges!");
            noBadges.setStyle(
                    "-fx-font-size: 14px; -fx-fill: #8B949E;");
            badgesRow.getChildren().add(noBadges);
        }

        Button viewAllBadges = new Button("View All Badges");
        viewAllBadges.getStyleClass().add("btn-secondary");
        viewAllBadges.setPrefWidth(180);
        viewAllBadges.setOnAction(e ->
                ScreenManager.getInstance().showBadges());

        content.getChildren().addAll(
                profileCard,
                statsTitle, statsRow,
                xpSection,
                badgesTitle, badgesRow,
                viewAllBadges
        );

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background: #0D1117;" +
                        "-fx-background-color: #0D1117;" +
                        "-fx-border-color: transparent;");

        root.setCenter(scroll);
        return root;
    }

    private VBox makeStatCard(String label,
                              String value, String color) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("card");
        card.setPrefWidth(110);
        card.setPrefHeight(90);

        Text v = new Text(value);
        v.setStyle("-fx-font-size: 22px; " +
                "-fx-font-weight: bold; -fx-fill: " + color + ";");

        Text l = new Text(label);
        l.setStyle("-fx-font-size: 11px; -fx-fill: #8B949E;");
        l.setWrappingWidth(100);

        card.getChildren().addAll(v, l);
        return card;
    }

    private VBox makeBadge(String name, String color) {
        VBox badge = new VBox(6);
        badge.setAlignment(Pos.CENTER);
        badge.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 2;" +
                        "-fx-padding: 12;" +
                        "-fx-min-width: 90px;");

        Text icon = new Text("★");
        icon.setStyle("-fx-font-size: 24px; " +
                "-fx-fill: " + color + ";");

        Text nameText = new Text(name);
        nameText.setStyle(
                "-fx-font-size: 11px; -fx-fill: white;");
        nameText.setWrappingWidth(80);

        badge.getChildren().addAll(icon, nameText);
        return badge;
    }
}
