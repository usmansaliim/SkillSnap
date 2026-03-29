package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.CareerDAO;
import com.skillsnap.database.GameDAO;
import com.skillsnap.models.career.CareerPath;
import com.skillsnap.models.game.MiniGame;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class GameLobbyScreen {

    private CareerPath career;
    private CareerDAO  careerDAO = new CareerDAO();
    private GameDAO    gameDAO   = new GameDAO();

    public GameLobbyScreen(CareerPath career) {
        this.career = career;
    }

    public Pane getLayout() {
        Player player = PlayerSession.getInstance().getCurrentPlayer();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── NAV BAR ───────────────────────────────────────────
        HBox navbar = new HBox();
        navbar.getStyleClass().add("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPrefHeight(60);
        navbar.setSpacing(16);
        navbar.setPadding(new Insets(0, 30, 0, 30));

        Button backBtn = new Button("← Careers");
        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2E75B6; -fx-cursor: hand;" +
                        "-fx-border-color: transparent; -fx-font-size: 14px;");
        backBtn.setOnAction(e ->
                ScreenManager.getInstance().showCareerMap());

        Text navTitle = new Text(career.getTitle());
        navTitle.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        navbar.getChildren().addAll(backBtn, navTitle);
        root.setTop(navbar);

        // ── MAIN CONTENT ──────────────────────────────────────
        VBox content = new VBox(28);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(36));

        // Career info header
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);

        VBox careerInfo = new VBox(8);
        careerInfo.setAlignment(Pos.CENTER_LEFT);
        careerInfo.getStyleClass().add("card");
        careerInfo.setPrefWidth(700);

        Text fieldTag = new Text(career.getField().toUpperCase());
        fieldTag.setStyle("-fx-font-size: 12px; " +
                "-fx-fill: #2E75B6; -fx-font-weight: bold;");

        Text titleText = new Text(career.getTitle());
        titleText.setStyle("-fx-font-size: 22px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        Text descText = new Text(career.getDescription());
        descText.setStyle("-fx-font-size: 14px; -fx-fill: #8B949E;");
        descText.setWrappingWidth(640);

        HBox statsRow = new HBox(30);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        Text salaryText = new Text("PKR " +
                String.format("%,.0f", career.getAvgSalaryPKR()) +
                " / month");
        salaryText.setStyle(
                "-fx-font-size: 14px; -fx-fill: #3FB950;");

        Text demandText = new Text("Market Demand: " +
                career.getMarketDemand() + "/10");
        demandText.setStyle(
                "-fx-font-size: 14px; -fx-fill: #E3B341;");

        statsRow.getChildren().addAll(salaryText, demandText);
        careerInfo.getChildren().addAll(
                fieldTag, titleText, descText, statsRow);
        header.getChildren().add(careerInfo);

        // ── GAMES LIST ────────────────────────────────────────
        Text gamesTitle = new Text("Available Games");
        gamesTitle.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        ArrayList<MiniGame> games =
                careerDAO.getGamesForCareer(career.getCareerId());

        VBox gamesList = new VBox(16);
        gamesList.setAlignment(Pos.CENTER);
        gamesList.setMaxWidth(700);

        for (MiniGame game : games) {
            gamesList.getChildren().add(
                    makeGameRow(game, player));
        }

        content.getChildren().addAll(
                header, gamesTitle, gamesList);

        // Wrap in scroll
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background: #0D1117;" +
                        "-fx-background-color: #0D1117;" +
                        "-fx-border-color: transparent;");

        root.setCenter(scroll);
        return root;
    }

    // ── Game row card ─────────────────────────────────────────
    private HBox makeGameRow(MiniGame game, Player player) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("card");
        row.setPrefWidth(700);
        row.setPadding(new Insets(16));

        // Difficulty color
        String diffColor = switch (game.getDifficulty()) {
            case "Easy"   -> "#3FB950";
            case "Medium" -> "#E3B341";
            case "Hard"   -> "#F85149";
            default       -> "#8B949E";
        };

        Label diffTag = new Label(game.getDifficulty());
        diffTag.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + diffColor + ";" +
                        "-fx-border-color: " + diffColor + ";" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 4 12;" +
                        "-fx-font-size: 12px;" +
                        "-fx-min-width: 70px;" +
                        "-fx-alignment: center;");

        // Game info
        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);

        Text gameTitle = new Text(game.getTitle());
        gameTitle.setStyle("-fx-font-size: 15px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        Text gameDesc = new Text(game.getDescription());
        gameDesc.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");
        gameDesc.setWrappingWidth(400);

        HBox gameMeta = new HBox(16);
        Text timeText = new Text("⏱ " +
                game.getTimeLimitSec() + "s");
        timeText.setStyle(
                "-fx-font-size: 12px; -fx-fill: #8B949E;");
        Text xpText = new Text("+" + game.getXpReward() + " XP");
        xpText.setStyle(
                "-fx-font-size: 12px; -fx-fill: #2E75B6;");

        // Best score
        int best = new GameDAO().getBestScore(
                PlayerSession.getInstance()
                        .getCurrentPlayer().getPlayerId(),
                game.getGameId());
        Text bestText = new Text(best > 0 ?
                "Best: " + best + "/" + game.getMaxScore() :
                "Not played yet");
        bestText.setStyle(
                "-fx-font-size: 12px; -fx-fill: #3FB950;");

        gameMeta.getChildren().addAll(
                timeText, xpText, bestText);
        info.getChildren().addAll(
                gameTitle, gameDesc, gameMeta);

        // Play button
        Button playBtn = new Button("Play");
        playBtn.getStyleClass().add("btn-primary");
        playBtn.setStyle(playBtn.getStyle() +
                "-fx-font-size: 13px; -fx-padding: 8 24;");
        playBtn.setOnAction(e ->
                ScreenManager.getInstance()
                        .showGame(game, career));

        row.getChildren().addAll(diffTag, info, playBtn);
        return row;
    }
}