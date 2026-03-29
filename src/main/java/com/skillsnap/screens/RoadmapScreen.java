package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.CareerDAO;
import com.skillsnap.models.career.CareerPath;
import com.skillsnap.models.career.RoadmapStep;
import com.skillsnap.models.career.SeniorAdvice;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class RoadmapScreen {

    private CareerPath career;
    private CareerDAO  careerDAO = new CareerDAO();

    public RoadmapScreen(CareerPath career) {
        this.career = career;
    }

    public Pane getLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── NAV ───────────────────────────────────────────────
        HBox navbar = new HBox();
        navbar.getStyleClass().add("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPrefHeight(60);
        navbar.setSpacing(16);
        navbar.setPadding(new Insets(0, 30, 0, 30));

        Button backBtn = new Button("← Back");
        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2E75B6; -fx-cursor: hand;" +
                        "-fx-border-color: transparent; -fx-font-size:14px;");
        backBtn.setOnAction(e ->
                ScreenManager.getInstance().showCareerMap());

        Text navTitle = new Text(
                career.getTitle() + " — Roadmap");
        navTitle.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        navbar.getChildren().addAll(backBtn, navTitle);
        root.setTop(navbar);

        // ── CONTENT ───────────────────────────────────────────
        VBox content = new VBox(32);
        content.setPadding(new Insets(36));
        content.setAlignment(Pos.TOP_CENTER);

        // ── ROADMAP STEPS ─────────────────────────────────────
        Text roadmapTitle = new Text("Your Learning Path");
        roadmapTitle.setStyle("-fx-font-size: 22px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        ArrayList<RoadmapStep> steps =
                careerDAO.getRoadmap(career.getCareerId());

        VBox stepsBox = new VBox(16);
        stepsBox.setMaxWidth(700);

        for (RoadmapStep step : steps) {
            stepsBox.getChildren().add(makeStepCard(step));
        }

        // ── SENIOR ADVICE ─────────────────────────────────────
        Text adviceTitle = new Text("Advice from Seniors");
        adviceTitle.setStyle("-fx-font-size: 22px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        ArrayList<SeniorAdvice> adviceList =
                careerDAO.getSeniorAdvice(career.getCareerId());

        VBox adviceBox = new VBox(16);
        adviceBox.setMaxWidth(700);

        for (SeniorAdvice advice : adviceList) {
            adviceBox.getChildren().add(
                    makeAdviceCard(advice));
        }

        // ── FOOTER BUTTON ─────────────────────────────────────
        Button exploreBtn = new Button(
                "Explore More Careers");
        exploreBtn.getStyleClass().add("btn-primary");
        exploreBtn.setPrefWidth(220);
        exploreBtn.setOnAction(e ->
                ScreenManager.getInstance().showCareerMap());

        content.getChildren().addAll(
                roadmapTitle, stepsBox,
                adviceTitle, adviceBox,
                exploreBtn);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background: #0D1117;" +
                        "-fx-background-color: #0D1117;" +
                        "-fx-border-color: transparent;");

        root.setCenter(scroll);
        return root;
    }

    // ── Step card ─────────────────────────────────────────────
    private HBox makeStepCard(RoadmapStep step) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.TOP_LEFT);
        row.getStyleClass().add("card");
        row.setMaxWidth(700);

        // Step number circle
        VBox numCircle = new VBox();
        numCircle.setAlignment(Pos.CENTER);
        numCircle.setStyle(
                "-fx-background-color: #1F3864;" +
                        "-fx-background-radius: 24;" +
                        "-fx-border-color: #2E75B6;" +
                        "-fx-border-radius: 24;" +
                        "-fx-border-width: 2;" +
                        "-fx-min-width: 48px;" +
                        "-fx-min-height: 48px;" +
                        "-fx-max-width: 48px;" +
                        "-fx-max-height: 48px;");

        Text numText = new Text(
                String.valueOf(step.getStepNumber()));
        numText.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: #2E75B6;");
        numCircle.getChildren().add(numText);

        // Step info
        VBox info = new VBox(6);
        HBox.setHgrow(info, Priority.ALWAYS);

        Text stepTitle = new Text(step.getTitle());
        stepTitle.setStyle("-fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        Text stepDesc = new Text(step.getDescription());
        stepDesc.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");
        stepDesc.setWrappingWidth(520);

        HBox meta = new HBox(16);
        Text duration = new Text("⏱ " + step.getDuration());
        duration.setStyle(
                "-fx-font-size: 12px; -fx-fill: #E3B341;");

        Text resource = new Text(
                "📚 " + step.getResourceTitle());
        resource.setStyle(
                "-fx-font-size: 12px; -fx-fill: #2E75B6;");

        meta.getChildren().addAll(duration, resource);
        info.getChildren().addAll(
                stepTitle, stepDesc, meta);

        row.getChildren().addAll(numCircle, info);
        return row;
    }

    // ── Advice card ───────────────────────────────────────────
    private VBox makeAdviceCard(SeniorAdvice advice) {
        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setMaxWidth(700);

        // Senior info header
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox seniorInfo = new VBox(3);
        Text name = new Text(advice.getSeniorName());
        name.setStyle("-fx-font-size: 15px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        Text role = new Text(
                advice.getCurrentRole() +
                        " @ " + advice.getCompany());
        role.setStyle(
                "-fx-font-size: 13px; -fx-fill: #2E75B6;");

        Text exp = new Text(
                advice.getUniversity() + "  •  " +
                        advice.getYearsExp() + " years experience");
        exp.setStyle(
                "-fx-font-size: 12px; -fx-fill: #8B949E;");

        seniorInfo.getChildren().addAll(name, role, exp);
        header.getChildren().add(seniorInfo);

        // Advice text
        Text adviceText = new Text(
                "\"" + advice.getAdviceText() + "\"");
        adviceText.setStyle(
                "-fx-font-size: 14px; -fx-fill: #C9D1D9;" +
                        "-fx-font-style: italic;");
        adviceText.setWrappingWidth(640);

        card.getChildren().addAll(header, adviceText);
        return card;
    }
}
