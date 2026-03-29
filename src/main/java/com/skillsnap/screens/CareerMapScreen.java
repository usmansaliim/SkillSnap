package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.CareerDAO;
import com.skillsnap.models.career.CareerPath;
import com.skillsnap.models.player.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class CareerMapScreen {

    private CareerDAO careerDAO = new CareerDAO();

    public Pane getLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── NAV BAR ───────────────────────────────────────────
        HBox navbar = new HBox();
        navbar.getStyleClass().add("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPrefHeight(60);
        navbar.setSpacing(20);

        Button backBtn = new Button("← Home");
        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2E75B6;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-size: 14px;"
        );
        backBtn.setOnAction(e ->
                ScreenManager.getInstance().showHome());

        Text logo = new Text("Choose Your Career");
        logo.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: white;");

        navbar.getChildren().addAll(backBtn, logo);
        root.setTop(navbar);

        // ── CAREER GRID ───────────────────────────────────────
        VBox content = new VBox(24);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(30));

        Text subtitle = new Text(
                "Play games to discover where your skills shine");
        subtitle.getStyleClass().add("subtitle-text");

        // Load careers from DB
        ArrayList<CareerPath> careers = careerDAO.getAllCareers();

        // Grid — 4 cards per row
        FlowPane grid = new FlowPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));

        for (CareerPath career : careers) {
            grid.getChildren().add(makeCareerCard(career));
        }

        // Wrap grid in ScrollPane for many careers
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background: #0D1117;" +
                        "-fx-background-color: #0D1117;" +
                        "-fx-border-color: transparent;");
        scroll.setPrefHeight(560);

        content.getChildren().addAll(subtitle, scroll);
        root.setCenter(content);

        return root;
    }

    // ── Career card ───────────────────────────────────────────
    private VBox makeCareerCard(CareerPath career) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("career-card");

        // Field tag
        Label fieldTag = new Label(career.getField());
        fieldTag.setStyle(
                "-fx-background-color: #1F3864;" +
                        "-fx-text-fill: #2E75B6;" +
                        "-fx-padding: 4 10;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 11px;");

        // Career title
        Text title = new Text(career.getTitle());
        title.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: white;");
        title.setWrappingWidth(160);

        // Market demand
        Text demand = new Text("Demand: " +
                career.getMarketDemand() + "/10");
        demand.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-fill: #3FB950;");

        // Salary
        Text salary = new Text("PKR " +
                String.format("%,.0f", career.getAvgSalaryPKR()));
        salary.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-fill: #8B949E;");

        // Play button
        Button playBtn = new Button("Play Games");
        playBtn.getStyleClass().add("btn-primary");
        playBtn.setStyle(playBtn.getStyle() +
                "-fx-font-size: 12px;" +
                "-fx-padding: 8 20;");

        // Store career in button for navigation
        // (GameLobbyScreen will receive this career)
        playBtn.setOnAction(e -> {
            // Will implement when GameLobbyScreen is built
            ScreenManager.getInstance().showGameLobby(career);
        });

        card.getChildren().addAll(
                fieldTag, title, demand, salary, playBtn);
        return card;
    }
}
