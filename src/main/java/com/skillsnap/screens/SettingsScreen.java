package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.PlayerDAO;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import com.skillsnap.utils.AnimationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import com.skillsnap.utils.SoundEngine;

public class SettingsScreen {

    private PlayerDAO playerDAO = new PlayerDAO();

    public Pane getLayout() {
        Player player = PlayerSession.getInstance().getCurrentPlayer();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0D1117;");

        // ── NAV ───────────────────────────────────────────────
        HBox navbar = new HBox();
        navbar.getStyleClass().add("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPrefHeight(60);
        navbar.setSpacing(16);
        navbar.setPadding(new Insets(0, 30, 0, 30));

        Button backBtn = new Button("← Profile");
        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #2E75B6; -fx-cursor: hand;" +
                        "-fx-border-color: transparent; -fx-font-size: 14px;");
        backBtn.setOnAction(e ->
                ScreenManager.getInstance().showProfile());

        Text navTitle = new Text("Settings");
        navTitle.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        navbar.getChildren().addAll(backBtn, navTitle);
        root.setTop(navbar);

        // ── CONTENT ───────────────────────────────────────────
        VBox content = new VBox(24);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(36));

        // ── CHANGE USERNAME ───────────────────────────────────
        VBox usernameCard = makeSection("Change Username");

        Text currentUser = new Text(
                "Current username:  @" + player.getUsername());
        currentUser.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");

        TextField newUsernameField = new TextField();
        newUsernameField.setPromptText("New username");
        newUsernameField.getStyleClass().add("input-field");
        newUsernameField.setMaxWidth(340);

        Text usernameMsg = new Text("");
        usernameMsg.setStyle("-fx-font-size: 13px;");

        Button saveUsernameBtn = new Button("Update Username");
        saveUsernameBtn.getStyleClass().add("btn-primary");
        saveUsernameBtn.setPrefWidth(200);
        AnimationUtils.addHoverScale(saveUsernameBtn);

        saveUsernameBtn.setOnAction(e -> {
            String newUsername = newUsernameField.getText().trim();

            if (newUsername.isEmpty()) {
                showMsg(usernameMsg, "Username cannot be empty.", false);
                return;
            }
            if (newUsername.length() < 3) {
                showMsg(usernameMsg, "Username must be at least 3 characters.", false);
                return;
            }
            if (playerDAO.usernameExists(newUsername)) {
                showMsg(usernameMsg, "That username is already taken.", false);
                return;
            }

            boolean ok = playerDAO.updateUsername(
                    player.getPlayerId(), newUsername);
            if (ok) {
                // Refresh session
                Player updated = playerDAO.getPlayerById(
                        player.getPlayerId());
                if (updated != null)
                    PlayerSession.getInstance().login(updated);
                currentUser.setText(
                        "Current username:  @" + newUsername);
                newUsernameField.clear();
                showMsg(usernameMsg, "Username updated!", true);
            } else {
                showMsg(usernameMsg, "Update failed. Try again.", false);
            }
        });

        usernameCard.getChildren().addAll(
                currentUser, newUsernameField,
                usernameMsg, saveUsernameBtn);

        // ── CHANGE PASSWORD ───────────────────────────────────
        VBox passwordCard = makeSection("Change Password");

        PasswordField currentPassField = new PasswordField();
        currentPassField.setPromptText("Current password");
        currentPassField.getStyleClass().add("input-field");
        currentPassField.setMaxWidth(340);

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("New password");
        newPassField.getStyleClass().add("input-field");
        newPassField.setMaxWidth(340);

        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirm new password");
        confirmPassField.getStyleClass().add("input-field");
        confirmPassField.setMaxWidth(340);

        Text passwordMsg = new Text("");
        passwordMsg.setStyle("-fx-font-size: 13px;");

        Button savePassBtn = new Button("Update Password");
        savePassBtn.getStyleClass().add("btn-primary");
        savePassBtn.setPrefWidth(200);
        AnimationUtils.addHoverScale(savePassBtn);

        savePassBtn.setOnAction(e -> {
            String current = currentPassField.getText();
            String newPass = newPassField.getText();
            String confirm = confirmPassField.getText();

            if (current.isEmpty() || newPass.isEmpty()
                    || confirm.isEmpty()) {
                showMsg(passwordMsg,
                        "Please fill in all fields.", false);
                return;
            }
            if (newPass.length() < 6) {
                showMsg(passwordMsg,
                        "New password must be at least 6 characters.", false);
                return;
            }
            if (!newPass.equals(confirm)) {
                showMsg(passwordMsg,
                        "Passwords do not match.", false);
                return;
            }

            boolean ok = playerDAO.updatePassword(
                    player.getPlayerId(), current, newPass);
            if (ok) {
                currentPassField.clear();
                newPassField.clear();
                confirmPassField.clear();
                showMsg(passwordMsg, "Password updated!", true);
            } else {
                showMsg(passwordMsg,
                        "Current password is incorrect.", false);
            }
        });

        passwordCard.getChildren().addAll(
                currentPassField, newPassField,
                confirmPassField, passwordMsg, savePassBtn);

        // ── CHOOSE AVATAR ─────────────────────────────────────
        VBox avatarCard = makeSection("Choose Avatar");

        Text avatarSub = new Text(
                "Current avatar: #" + player.getAvatarId());
        avatarSub.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");

        // Avatar grid — 8 emoji avatars
        String[] avatarEmojis = {
                "😎", "🚀", "🎯", "💡",
                "🔥", "⚡", "🌟", "🎮"
        };
        String[] avatarColors = {
                "#2E75B6", "#3FB950", "#E3B341", "#BC8CFF",
                "#F85149", "#58A6FF", "#FFA657", "#FF7B72"
        };

        final int[] selectedAvatar = {player.getAvatarId()};

        GridPane avatarGrid = new GridPane();
        avatarGrid.setHgap(12);
        avatarGrid.setVgap(12);
        avatarGrid.setAlignment(Pos.CENTER);

        Text avatarMsg = new Text("");
        avatarMsg.setStyle("-fx-font-size: 13px;");

        for (int i = 0; i < avatarEmojis.length; i++) {
            final int avatarId = i + 1;
            VBox avatarOption = new VBox(6);
            avatarOption.setAlignment(Pos.CENTER);
            avatarOption.setCursor(
                    javafx.scene.Cursor.HAND);
            avatarOption.setStyle(
                    "-fx-background-color: " +
                            (avatarId == player.getAvatarId() ?
                                    "#1F3864" : "#161B22") + ";" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-color: " +
                            (avatarId == player.getAvatarId() ?
                                    avatarColors[i] : "#30363D") + ";" +
                            "-fx-border-radius: 12;" +
                            "-fx-border-width: 2;" +
                            "-fx-padding: 12;" +
                            "-fx-min-width: 72px;");

            Text emoji = new Text(avatarEmojis[i]);
            emoji.setStyle("-fx-font-size: 28px;");

            Text idText = new Text("#" + avatarId);
            idText.setStyle(
                    "-fx-font-size: 11px; -fx-fill: #8B949E;");

            avatarOption.getChildren().addAll(emoji, idText);

            // Click to select
            String color = avatarColors[i];
            avatarOption.setOnMouseClicked(ev -> {
                selectedAvatar[0] = avatarId;

                // Reset all borders
                avatarGrid.getChildren().forEach(node -> {
                    if (node instanceof VBox v) {
                        v.setStyle(
                                "-fx-background-color: #161B22;" +
                                        "-fx-background-radius: 12;" +
                                        "-fx-border-color: #30363D;" +
                                        "-fx-border-radius: 12;" +
                                        "-fx-border-width: 2;" +
                                        "-fx-padding: 12;" +
                                        "-fx-min-width: 72px;");
                    }
                });

                // Highlight selected
                avatarOption.setStyle(
                        "-fx-background-color: #1F3864;" +
                                "-fx-background-radius: 12;" +
                                "-fx-border-color: " + color + ";" +
                                "-fx-border-radius: 12;" +
                                "-fx-border-width: 2;" +
                                "-fx-padding: 12;" +
                                "-fx-min-width: 72px;");

                AnimationUtils.bounce(avatarOption);
            });

            avatarGrid.add(avatarOption, i % 4, i / 4);
        }

        Button saveAvatarBtn = new Button("Save Avatar");
        saveAvatarBtn.getStyleClass().add("btn-primary");
        saveAvatarBtn.setPrefWidth(200);
        AnimationUtils.addHoverScale(saveAvatarBtn);

        saveAvatarBtn.setOnAction(e -> {
            boolean ok = playerDAO.updateAvatar(
                    player.getPlayerId(), selectedAvatar[0]);
            if (ok) {
                Player updated = playerDAO.getPlayerById(
                        player.getPlayerId());
                if (updated != null)
                    PlayerSession.getInstance().login(updated);
                avatarSub.setText(
                        "Current avatar: #" + selectedAvatar[0]);
                showMsg(avatarMsg, "Avatar saved!", true);
                AnimationUtils.bounce(saveAvatarBtn);
            } else {
                showMsg(avatarMsg, "Save failed. Try again.", false);
            }
        });

        avatarCard.getChildren().addAll(
                avatarSub, avatarGrid, avatarMsg, saveAvatarBtn);

        // ── SOUND SETTINGS ────────────────────────────────────────
        VBox soundCard = makeSection("Sound Effects");

        javafx.scene.control.CheckBox soundToggle =
                new javafx.scene.control.CheckBox("Enable sound effects");
        soundToggle.setSelected(
                SoundEngine.getInstance().isSoundEnabled());
        soundToggle.setStyle(
                "-fx-text-fill: #C9D1D9; -fx-font-size: 14px;");
        soundToggle.selectedProperty().addListener(
                (obs, oldVal, newVal) -> {
                    SoundEngine.getInstance().setSoundEnabled(newVal);
                    if (newVal) SoundEngine.getInstance().playCorrect();
                });

        Text soundNote = new Text(
                "Sounds play on correct/wrong answers, badge unlocks and level ups.");
        soundNote.setStyle(
                "-fx-font-size: 12px; -fx-fill: #8B949E;");
        soundNote.setWrappingWidth(420);

        soundCard.getChildren().addAll(soundToggle, soundNote);

        content.getChildren().addAll(
                usernameCard, passwordCard, avatarCard, soundCard);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background: #0D1117;" +
                        "-fx-background-color: #0D1117;" +
                        "-fx-border-color: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.setCenter(scroll);
        AnimationUtils.slideInFromRight(content);
        return root;


    }

    // ── Section card with title ───────────────────────────────
    private VBox makeSection(String title) {
        VBox card = new VBox(14);
        card.getStyleClass().add("card");
        card.setMaxWidth(500);
        card.setAlignment(Pos.TOP_LEFT);

        Text titleText = new Text(title);
        titleText.setStyle(
                "-fx-font-size: 17px; -fx-font-weight: bold;" +
                        "-fx-fill: white;");

        // Divider line
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #30363D;");

        card.getChildren().addAll(titleText, sep);
        return card;
    }

    // ── Show feedback message ─────────────────────────────────
    private void showMsg(Text label, String msg, boolean success) {
        label.setText(msg);
        label.setStyle("-fx-font-size: 13px; -fx-fill: " +
                (success ? "#3FB950" : "#F85149") + ";");
    }
}
