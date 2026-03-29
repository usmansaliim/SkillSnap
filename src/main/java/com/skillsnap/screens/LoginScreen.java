package com.skillsnap.screens;
import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.PlayerDAO;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class LoginScreen {

    private PlayerDAO playerDAO = new PlayerDAO();

    public Pane getLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── LOGIN CARD ────────────────────────────────────────
        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("card");
        card.setMaxWidth(380);

        // Header
        Text heading = new Text("Welcome back");
        heading.getStyleClass().add("heading-text");

        Text sub = new Text("Login to continue your journey");
        sub.getStyleClass().add("subtitle-text");

        // Fields
        Label userLabel = new Label("Username");
        userLabel.getStyleClass().add("body-text");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.getStyleClass().add("input-field");

        Label passLabel = new Label("Password");
        passLabel.getStyleClass().add("body-text");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter password");
        passField.getStyleClass().add("input-field");

        // Error message
        Text errorText = new Text();
        errorText.getStyleClass().add("error-text");

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setPrefWidth(320);
        loginBtn.setOnAction(e -> handleLogin(
                usernameField.getText().trim(),
                passField.getText().trim(),
                errorText
        ));

        // Register link
        Button registerLink = new Button("Don't have an account? Register");
        registerLink.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2E75B6;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;"
        );
        registerLink.setOnAction(e ->
                ScreenManager.getInstance().showRegister());

        card.getChildren().addAll(
                heading, sub,
                userLabel, usernameField,
                passLabel, passField,
                errorText, loginBtn, registerLink
        );

        // Center the card
        BorderPane.setAlignment(card, Pos.CENTER);
        VBox wrapper = new VBox(card);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setStyle("-fx-background-color: #0D1117;");

        root.setCenter(wrapper);
        return root;
    }

    private void handleLogin(String username, String password,
                             Text errorText) {
        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            errorText.setText("Please fill in all fields.");
            return;
        }

        // Query database
        Player player = playerDAO.login(username, password);

        if (player != null) {
            // Store in session
            PlayerSession.getInstance().login(player);
            // Go to home
            ScreenManager.getInstance().showHome();
        } else {
            errorText.setText("Wrong username or password.");
        }
    }
}
