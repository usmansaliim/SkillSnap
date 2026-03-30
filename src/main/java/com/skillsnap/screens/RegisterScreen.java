package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.PlayerDAO;
import com.skillsnap.utils.ValidationUtils;
import com.skillsnap.models.player.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class RegisterScreen {

    private PlayerDAO playerDAO = new PlayerDAO();

    public Pane getLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── REGISTER CARD ─────────────────────────────────────
        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("card");
        card.setMaxWidth(400);

        Text heading = new Text("Create Account");
        heading.getStyleClass().add("heading-text");

        Text sub = new Text("Start discovering your career path");
        sub.getStyleClass().add("subtitle-text");

        // Fields
        TextField fullNameField  = makeField("Full Name",   "Muhammad Usman Saleem");
        TextField usernameField  = makeField("Username",    "Choose a username");
        TextField emailField     = makeField("Email",       "your@email.com");
        TextField uniField       = makeField("University",  "NUST, FAST, LUMS...");
        PasswordField passField  = new PasswordField();
        passField.setPromptText("Choose a password");
        passField.getStyleClass().add("input-field");

        Label passLabel = new Label("Password");
        passLabel.getStyleClass().add("body-text");

        // Error/success
        Text feedbackText = new Text();

        // Register button
        Button registerBtn = new Button("Create Account");
        registerBtn.getStyleClass().add("btn-primary");
        registerBtn.setPrefWidth(340);
        registerBtn.setOnAction(e -> handleRegister(
                fullNameField.getText().trim(),
                usernameField.getText().trim(),
                emailField.getText().trim(),
                uniField.getText().trim(),
                passField.getText().trim(),
                feedbackText
        ));

        // Back to login
        Button loginLink = new Button("Already have an account? Login");
        loginLink.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2E75B6;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;"
        );
        loginLink.setOnAction(e ->
                ScreenManager.getInstance().showLogin());

        card.getChildren().addAll(
                heading, sub,
                makeLabel("Full Name"),    fullNameField,
                makeLabel("Username"),     usernameField,
                makeLabel("Email"),        emailField,
                makeLabel("University"),   uniField,
                passLabel,                 passField,
                feedbackText, registerBtn, loginLink
        );

        VBox wrapper = new VBox(card);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setStyle("-fx-background-color: #0D1117;");
        root.setCenter(wrapper);
        return root;
    }

    private void handleRegister(String fullName,
                                String username,
                                String email,
                                String university,
                                String password,
                                Text feedbackText) {

        // Validate each field with specific messages
        String nameError = ValidationUtils.getFullNameError(fullName);
        if (nameError != null) {
            showError(feedbackText, nameError); return;
        }

        String userError = ValidationUtils.getUsernameError(username);
        if (userError != null) {
            showError(feedbackText, userError); return;
        }

        String emailError = ValidationUtils.getEmailError(email);
        if (emailError != null) {
            showError(feedbackText, emailError); return;
        }

        String passError = ValidationUtils.getPasswordError(password);
        if (passError != null) {
            showError(feedbackText, passError); return;
        }

        if (playerDAO.usernameExists(username)) {
            showError(feedbackText,
                    "Username already taken. Try another."); return;
        }

        boolean success = playerDAO.register(
                username, password, fullName, email, university, 1);

        if (success) {
            Player player = playerDAO.login(username, password);
            PlayerSession.getInstance().login(player);
            ScreenManager.getInstance().showHome();
        } else {
            showError(feedbackText,
                    "Registration failed. Please try again.");
        }
    }

    private void showError(Text text, String message) {
        text.getStyleClass().setAll("error-text");
        text.setText(message);
    }

    // ── Helpers ───────────────────────────────────────────────
    private TextField makeField(String label, String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("input-field");
        return tf;
    }

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("body-text");
        return l;
    }
}