package com.skillsnap.screens;
import com.skillsnap.app.ScreenManager;
import com.skillsnap.utils.AnimationUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class WelcomeScreen {

    public Pane getLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── CENTER CONTENT ────────────────────────────────────
        VBox center = new VBox(24);
        center.setAlignment(Pos.CENTER);

        // Logo / title
        Text title = new Text("SkillSnap");
        title.getStyleClass().add("title-text");

        // Tagline
        Text tagline = new Text("Discover your career through play");
        tagline.getStyleClass().add("subtitle-text");

        // Divider space
        Label spacer = new Label();
        spacer.setPrefHeight(20);

        // Buttons
        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setPrefWidth(240);
        loginBtn.setOnAction(e ->
                ScreenManager.getInstance().showLogin());

        Button registerBtn = new Button("Create Account");
        registerBtn.getStyleClass().add("btn-secondary");
        registerBtn.setPrefWidth(240);
        registerBtn.setOnAction(e ->
                ScreenManager.getInstance().showRegister());

        // Version tag
        Text version = new Text(
                "SkillSnap v1.0  •  Muhammad Usman Saleem  •  " +
                        "NUST SEECS  •  Semester 2 OOP Project");
        version.getStyleClass().add("subtitle-text");
        version.setStyle("-fx-font-size: 11px;");

        center.getChildren().addAll(
                title, tagline, spacer,
                loginBtn, registerBtn, version
        );

        root.setCenter(center);

        AnimationUtils.fadeIn(center);
        return root;

    }


}
