package com.skillsnap.screens;

import com.skillsnap.models.game.GameResult;
import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.GameDAO;
import com.skillsnap.models.career.CareerPath;
import com.skillsnap.models.career.CareerResult;
import com.skillsnap.models.game.*;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import com.skillsnap.utils.AnimationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class ResultsScreen {

    private GameResult result;
    private CareerPath career;
    private GameDAO    gameDAO = new GameDAO();

    public ResultsScreen(GameResult result, CareerPath career) {
        this.result = result;
        this.career = career;
    }

    public Pane getLayout() {
        Player player =
                PlayerSession.getInstance().getCurrentPlayer();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        VBox content = new VBox(28);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));

        // ── SCORE DISPLAY ─────────────────────────────────────
        double pct =
                (result.getScore() * 100.0) / result.getMaxScore();

        Text resultMsg = new Text(result.getMessage());
        resultMsg.setStyle("-fx-font-size: 28px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        // Big score circle (simulated with styled label)
        VBox scoreCircle = new VBox();
        scoreCircle.setAlignment(Pos.CENTER);
        scoreCircle.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-background-radius: 80;" +
                        "-fx-border-color: #2E75B6;" +
                        "-fx-border-radius: 80;" +
                        "-fx-border-width: 4;" +
                        "-fx-padding: 30;");
        scoreCircle.setPrefSize(160, 160);
        scoreCircle.setMaxSize(160, 160);

        Text scoreNum = new Text(
                result.getScore() + "/" + result.getMaxScore());
        scoreNum.setStyle("-fx-font-size: 28px; " +
                "-fx-font-weight: bold; -fx-fill: #2E75B6;");

        Text scorePct = new Text(
                String.format("%.0f%%", pct));
        scorePct.setStyle(
                "-fx-font-size: 16px; -fx-fill: #8B949E;");

        scoreCircle.getChildren().addAll(scoreNum, scorePct);

        // ── STATS ROW ─────────────────────────────────────────
        HBox stats = new HBox(20);
        stats.setAlignment(Pos.CENTER);

        stats.getChildren().addAll(
                makeStatBox("XP Earned",
                        "+" + result.getXpEarned(), "#2E75B6"),
                makeStatBox("Time Taken",
                        result.getTimeTakenSec() + "s", "#8B949E"),
                makeStatBox("Career",
                        career.getTitle(), "#3FB950")
        );

        // ── CAREER SUGGESTION CHECK ───────────────────────────
        int gamesPlayed = gameDAO.getTotalGamesPlayed(
                player.getPlayerId());

        VBox suggestionBox = new VBox(12);
        suggestionBox.setAlignment(Pos.CENTER);

        if (gamesPlayed >= 3) {
            ArrayList<CareerResult> suggestions =
                    gameDAO.getSuggestedCareers(
                            player.getPlayerId());

            if (!suggestions.isEmpty()) {
                CareerResult top = suggestions.get(0);
                Text suggTitle = new Text(
                        "Based on your performance...");
                suggTitle.setStyle(
                        "-fx-font-size: 16px; -fx-fill: #8B949E;");

                Text suggCareer = new Text(
                        top.getTitle() + " looks like your best match!");
                suggCareer.setStyle(
                        "-fx-font-size: 20px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-fill: #3FB950;");

                Text matchScore = new Text(
                        String.format("%.1f%% compatibility",
                                top.getMatchScore()));
                matchScore.setStyle(
                        "-fx-font-size: 14px; -fx-fill: #8B949E;");

                Button roadmapBtn = new Button(
                        "View Roadmap for " + top.getTitle());
                roadmapBtn.getStyleClass().add("btn-primary");
                roadmapBtn.setPrefWidth(280);
                roadmapBtn.setOnAction(e -> {
                    // Save suggestion then show roadmap
                    gameDAO.saveSuggestion(
                            player.getPlayerId(),
                            top.getCareerId(),
                            top.getMatchScore(), 1);
                    ScreenManager.getInstance()
                            .showRoadmap(career);
                });

                suggestionBox.getChildren().addAll(
                        suggTitle, suggCareer,
                        matchScore, roadmapBtn);
            }
        } else {
            Text moreGames = new Text(
                    "Play " + (3 - gamesPlayed) +
                            " more games to get your career suggestion!");
            moreGames.setStyle(
                    "-fx-font-size: 15px; -fx-fill: #8B949E;");
            suggestionBox.getChildren().add(moreGames);
        }

        // ── BUTTONS ───────────────────────────────────────────
        HBox buttons = new HBox(16);
        buttons.setAlignment(Pos.CENTER);

        Button homeBtn = new Button("Home");
        homeBtn.getStyleClass().add("btn-secondary");
        homeBtn.setPrefWidth(140);
        homeBtn.setOnAction(e ->
                ScreenManager.getInstance().showHome());

        Button careersBtn = new Button("More Careers");
        careersBtn.getStyleClass().add("btn-primary");
        careersBtn.setPrefWidth(160);
        careersBtn.setOnAction(e ->
                ScreenManager.getInstance().showCareerMap());

        buttons.getChildren().addAll(homeBtn, careersBtn);

        content.getChildren().addAll(
                resultMsg, scoreCircle, stats,
                suggestionBox, buttons);

        root.setCenter(content);
        AnimationUtils.fadeIn(scoreCircle);
        AnimationUtils.bounce(scoreCircle);
        return root;


    }

    private VBox makeStatBox(String label,
                             String value, String color) {
        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("card");
        box.setPrefWidth(180);

        Text v = new Text(value);
        v.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: " + color + ";");
        v.setWrappingWidth(160);

        Text l = new Text(label);
        l.setStyle("-fx-font-size: 12px; -fx-fill: #8B949E;");

        box.getChildren().addAll(v, l);
        return box;
    }
}
