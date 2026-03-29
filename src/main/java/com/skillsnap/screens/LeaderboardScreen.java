package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.DatabaseManager;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.sql.*;
import java.util.ArrayList;

public class LeaderboardScreen {

    public Pane getLayout() {
        Player currentPlayer =
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

        Text navTitle = new Text("Leaderboard");
        navTitle.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        navbar.getChildren().addAll(backBtn, navTitle);
        root.setTop(navbar);

        // ── CONTENT ───────────────────────────────────────────
        VBox content = new VBox(24);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(36));

        Text subtitle = new Text("Top players by XP");
        subtitle.setStyle(
                "-fx-font-size: 15px; -fx-fill: #8B949E;");

        // Load top 10 players
        ArrayList<LeaderboardEntry> entries =
                getTopPlayers();

        VBox list = new VBox(12);
        list.setMaxWidth(700);
        list.setAlignment(Pos.CENTER);

        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry entry = entries.get(i);
            boolean isCurrentPlayer =
                    entry.username.equals(
                            currentPlayer.getUsername());
            list.getChildren().add(
                    makeEntryRow(i + 1, entry, isCurrentPlayer));
        }

        if (entries.isEmpty()) {
            Text empty = new Text(
                    "No players yet. Be the first!");
            empty.setStyle(
                    "-fx-font-size: 16px; -fx-fill: #8B949E;");
            list.getChildren().add(empty);
        }

        content.getChildren().addAll(subtitle, list);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
                "-fx-background: #0D1117;" +
                        "-fx-background-color: #0D1117;" +
                        "-fx-border-color: transparent;");

        root.setCenter(scroll);
        return root;
    }

    // ── Leaderboard row ───────────────────────────────────────
    private HBox makeEntryRow(int rank,
                              LeaderboardEntry entry,
                              boolean isCurrentPlayer) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16));
        row.setMaxWidth(700);

        // Highlight current player
        String borderColor = isCurrentPlayer ?
                "#2E75B6" : "#30363D";
        String bgColor = isCurrentPlayer ?
                "#1C2128" : "#161B22";

        row.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: " +
                        (isCurrentPlayer ? "2" : "1") + ";");

        // Rank badge
        String rankColor = rank == 1 ? "#E3B341" :
                rank == 2 ? "#8B949E" :
                        rank == 3 ? "#F78166" : "#484F58";

        VBox rankBadge = new VBox();
        rankBadge.setAlignment(Pos.CENTER);
        rankBadge.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-min-width: 40px;");

        Text rankText = new Text("#" + rank);
        rankText.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; -fx-fill: " +
                rankColor + ";");
        rankBadge.getChildren().add(rankText);

        // Avatar circle
        VBox avatar = new VBox();
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
                "-fx-background-color: #1F3864;" +
                        "-fx-background-radius: 20;" +
                        "-fx-min-width: 40px; -fx-min-height: 40px;" +
                        "-fx-max-width: 40px; -fx-max-height: 40px;");
        Text avatarLetter = new Text(
                String.valueOf(entry.fullName.charAt(0))
                        .toUpperCase());
        avatarLetter.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; -fx-fill: #2E75B6;");
        avatar.getChildren().add(avatarLetter);

        // Player info
        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);

        Text nameText = new Text(entry.fullName +
                (isCurrentPlayer ? "  (You)" : ""));
        nameText.setStyle("-fx-font-size: 15px; " +
                "-fx-font-weight: bold; -fx-fill: white;");

        Text usernameText = new Text("@" + entry.username +
                "  •  Level " + entry.level);
        usernameText.setStyle(
                "-fx-font-size: 12px; -fx-fill: #8B949E;");

        info.getChildren().addAll(nameText, usernameText);

        // XP
        Text xpText = new Text(entry.totalXP + " XP");
        xpText.setStyle("-fx-font-size: 16px; " +
                "-fx-font-weight: bold; -fx-fill: #E3B341;");

        row.getChildren().addAll(
                rankBadge, avatar, info, xpText);
        return row;
    }

    // ── Query top 10 players ──────────────────────────────────
    private ArrayList<LeaderboardEntry> getTopPlayers() {
        ArrayList<LeaderboardEntry> entries = new ArrayList<>();
        String sql =
                "SELECT username, full_name, total_xp, level " +
                        "FROM Player " +
                        "ORDER BY total_xp DESC LIMIT 10";
        try {
            Connection conn =
                    DatabaseManager.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entries.add(new LeaderboardEntry(
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getInt("total_xp"),
                        rs.getInt("level")
                ));
            }
        } catch (SQLException e) {
            System.out.println(
                    "Leaderboard query failed: " + e.getMessage());
        }
        return entries;
    }

    // ── Inner data class ──────────────────────────────────────
    private static class LeaderboardEntry {
        String username;
        String fullName;
        int    totalXP;
        int    level;

        LeaderboardEntry(String u, String f, int xp, int l) {
            username = u; fullName = f;
            totalXP = xp; level = l;
        }
    }
}
