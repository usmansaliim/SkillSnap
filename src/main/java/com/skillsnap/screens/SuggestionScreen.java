package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.engine.RecommendationEngine;
import com.skillsnap.engine.RecommendationEngine.Recommendation;
import com.skillsnap.models.career.CareerResult;
import com.skillsnap.models.career.RoadmapStep;
import com.skillsnap.models.career.SeniorAdvice;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class SuggestionScreen {

    private RecommendationEngine engine =
            new RecommendationEngine();

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
        navbar.setPadding(new Insets(0, 30, 0, 30));
        navbar.setSpacing(16);

        Button backBtn = new Button("← Home");
        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2E75B6; -fx-cursor: hand;" +
                        "-fx-border-color: transparent; -fx-font-size: 14px;");
        backBtn.setOnAction(e ->
                ScreenManager.getInstance().showHome());

        Text navTitle = new Text("Your Career Match");
        navTitle.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        navbar.getChildren().addAll(backBtn, navTitle);
        root.setTop(navbar);

        // ── GENERATE RECOMMENDATION ───────────────────────────
        Recommendation rec =
                engine.generate(player.getPlayerId());

        VBox content = new VBox(32);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(36));

        if (rec == null) {
            // Not enough games played yet
            VBox notEnough = new VBox(16);
            notEnough.setAlignment(Pos.CENTER);
            notEnough.getStyleClass().add("card");
            notEnough.setMaxWidth(500);
            notEnough.setPadding(new Insets(40));

            Text msg = new Text(
                    "Play at least 3 games to get your\n" +
                            "personalized career suggestion!");
            msg.setStyle("-fx-font-size: 18px; " +
                    "-fx-fill: #8B949E; -fx-text-alignment: center;");
            msg.setWrappingWidth(420);

            Button goPlay = new Button("Explore Careers");
            goPlay.getStyleClass().add("btn-primary");
            goPlay.setPrefWidth(200);
            goPlay.setOnAction(e ->
                    ScreenManager.getInstance().showCareerMap());

            notEnough.getChildren().addAll(msg, goPlay);
            content.getChildren().add(notEnough);

        } else {
            // ── TOP MATCH HERO CARD ───────────────────────────
            VBox heroCard = new VBox(16);
            heroCard.setAlignment(Pos.CENTER);
            heroCard.setMaxWidth(700);
            heroCard.setStyle(
                    "-fx-background-color: #161B22;" +
                            "-fx-background-radius: 16;" +
                            "-fx-border-color: #2E75B6;" +
                            "-fx-border-radius: 16;" +
                            "-fx-border-width: 2;" +
                            "-fx-padding: 32;");

            Text matchLabel = new Text(
                    "Your #1 Career Match");
            matchLabel.setStyle(
                    "-fx-font-size: 13px; " +
                            "-fx-fill: #2E75B6; " +
                            "-fx-font-weight: bold;");

            Text careerTitle = new Text(
                    rec.topCareer.getTitle());
            careerTitle.setStyle(
                    "-fx-font-size: 32px; " +
                            "-fx-font-weight: bold; -fx-fill: white;");

            Text careerDesc = new Text(
                    rec.topCareer.getDescription());
            careerDesc.setStyle(
                    "-fx-font-size: 14px; -fx-fill: #8B949E;");
            careerDesc.setWrappingWidth(600);

            // Match score bar
            VBox scoreSection = new VBox(8);
            scoreSection.setMaxWidth(600);

            Text scoreLabel = new Text(String.format(
                    "%.1f%% compatibility score",
                    rec.matchScore));
            scoreLabel.setStyle(
                    "-fx-font-size: 20px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-fill: #3FB950;");

            ProgressBar matchBar =
                    new ProgressBar(rec.matchScore / 100.0);
            matchBar.setPrefWidth(600);
            matchBar.setPrefHeight(12);
            matchBar.setStyle(
                    "-fx-accent: #3FB950;" +
                            "-fx-background-color: #30363D;" +
                            "-fx-background-radius: 6;");

            scoreSection.getChildren().addAll(
                    scoreLabel, matchBar);

            // Stats row
            HBox statsRow = new HBox(20);
            statsRow.setAlignment(Pos.CENTER);

            statsRow.getChildren().addAll(
                    makeInfoChip("Avg Salary",
                            "PKR " + String.format("%,.0f",
                                    rec.topCareer.getAvgSalaryPKR()),
                            "#3FB950"),
                    makeInfoChip("Market Demand",
                            rec.topCareer.getMarketDemand() + "/10",
                            "#E3B341"),
                    makeInfoChip("Field",
                            rec.topCareer.getField(),
                            "#2E75B6")
            );

            // Core skills
            Text skillsLabel = new Text("Core Skills:");
            skillsLabel.setStyle(
                    "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; -fx-fill: white;");

            HBox skillsRow = new HBox(10);
            skillsRow.setAlignment(Pos.CENTER);
            for (String skill : rec.coreSkills) {
                javafx.scene.control.Label chip = new javafx.scene.control.Label(
                        skill);
                chip.setStyle(
                        "-fx-background-color: #1F3864;" +
                                "-fx-text-fill: #2E75B6;" +
                                "-fx-padding: 4 12;" +
                                "-fx-background-radius: 20;" +
                                "-fx-font-size: 12px;");
                skillsRow.getChildren().add(chip);
            }

            Button viewRoadmapBtn =
                    new Button("View Full Roadmap");
            viewRoadmapBtn.getStyleClass().add("btn-primary");
            viewRoadmapBtn.setPrefWidth(200);
            viewRoadmapBtn.setOnAction(e ->
                    ScreenManager.getInstance()
                            .showRoadmap(rec.topCareer));

            heroCard.getChildren().addAll(
                    matchLabel, careerTitle, careerDesc,
                    scoreSection, statsRow,
                    skillsLabel, skillsRow,
                    viewRoadmapBtn);

            // ── ALL RESULTS TABLE ─────────────────────────────
            Text allTitle = new Text("All Career Matches");
            allTitle.setStyle(
                    "-fx-font-size: 20px; " +
                            "-fx-font-weight: bold; -fx-fill: white;");

            VBox resultsList = new VBox(10);
            resultsList.setMaxWidth(700);

            for (int i = 0;
                 i < rec.allResults.size(); i++) {
                CareerResult cr = rec.allResults.get(i);
                resultsList.getChildren().add(
                        makeResultRow(i + 1, cr));
            }

            content.getChildren().addAll(
                    heroCard, allTitle, resultsList);
        }

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background: #0D1117;" +
                        "-fx-background-color: #0D1117;" +
                        "-fx-border-color: transparent;");

        root.setCenter(scroll);
        return root;
    }

    // ── Result row ────────────────────────────────────────────
    private HBox makeResultRow(int rank,
                               CareerResult cr) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14));
        row.setMaxWidth(700);
        row.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #30363D;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;");

        Text rankText = new Text("#" + rank);
        rankText.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-fill: " + (rank == 1 ? "#E3B341" :
                        "#8B949E") + ";");
        rankText.setWrappingWidth(40);

        Text titleText = new Text(cr.getTitle());
        titleText.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; -fx-fill: white;");
        HBox.setHgrow(
                new VBox(titleText), Priority.ALWAYS);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        // Score bar
        ProgressBar bar =
                new ProgressBar(cr.getMatchScore() / 100.0);
        bar.setPrefWidth(150);
        bar.setPrefHeight(8);
        bar.setStyle(
                "-fx-accent: #2E75B6;" +
                        "-fx-background-color: #30363D;" +
                        "-fx-background-radius: 4;");

        Text scoreText = new Text(String.format(
                "%.1f%%", cr.getMatchScore()));
        scoreText.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-fill: #2E75B6;");
        scoreText.setWrappingWidth(60);

        row.getChildren().addAll(
                rankText, titleText, sp, bar, scoreText);
        return row;
    }

    // ── Info chip ─────────────────────────────────────────────
    private VBox makeInfoChip(String label,
                              String value,
                              String color) {
        VBox chip = new VBox(4);
        chip.setAlignment(Pos.CENTER);
        chip.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #30363D;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 12 20;");

        Text v = new Text(value);
        v.setStyle("-fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-fill: " + color + ";");

        Text l = new Text(label);
        l.setStyle(
                "-fx-font-size: 12px; -fx-fill: #8B949E;");

        chip.getChildren().addAll(v, l);
        return chip;
    }
}
