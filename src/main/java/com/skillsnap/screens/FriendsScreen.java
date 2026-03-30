package com.skillsnap.screens;

import com.skillsnap.app.ScreenManager;
import com.skillsnap.database.FriendDAO;
import com.skillsnap.models.player.Player;
import com.skillsnap.models.player.PlayerSession;
import com.skillsnap.utils.AnimationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.List;

public class FriendsScreen {

    private FriendDAO friendDAO = new FriendDAO();

    public Pane getLayout() {
        Player me = PlayerSession.getInstance().getCurrentPlayer();

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

        Text navTitle = new Text("Friends");
        navTitle.setStyle("-fx-font-size: 18px;" +
                "-fx-font-weight: bold; -fx-fill: white;");

        navbar.getChildren().addAll(backBtn, navTitle);
        root.setTop(navbar);

        // ── MAIN CONTENT ──────────────────────────────────────
        VBox content = new VBox(28);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(36));

        // ── SEARCH SECTION ────────────────────────────────────
        VBox searchCard = makeSection("Find a Friend");

        HBox searchRow = new HBox(12);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by username...");
        searchField.getStyleClass().add("input-field");
        searchField.setPrefWidth(280);

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("btn-primary");
        searchBtn.setPrefWidth(110);
        searchBtn.setPrefHeight(44);
        AnimationUtils.addHoverScale(searchBtn);

        searchRow.getChildren().addAll(searchField, searchBtn);

        // Search result area
        VBox searchResult = new VBox(10);
        searchResult.setAlignment(Pos.TOP_LEFT);

        searchBtn.setOnAction(e -> {
            String query = searchField.getText().trim();
            searchResult.getChildren().clear();
            if (query.isEmpty()) return;

            Player found = friendDAO.searchByUsername(
                    query, me.getPlayerId());
            if (found == null) {
                Text notFound = new Text(
                        "No player found with that username.");
                notFound.setStyle(
                        "-fx-font-size: 13px; -fx-fill: #8B949E;");
                searchResult.getChildren().add(notFound);
                return;
            }

            String status = friendDAO.getStatus(
                    me.getPlayerId(), found.getPlayerId());
            String reverseStatus = friendDAO.getStatus(
                    found.getPlayerId(), me.getPlayerId());

            searchResult.getChildren().add(
                    makePlayerCard(found, me, status,
                            reverseStatus, searchResult, null));
        });

        // Allow pressing Enter to search
        searchField.setOnAction(e -> searchBtn.fire());

        searchCard.getChildren().addAll(searchRow, searchResult);

        // ── PENDING REQUESTS ──────────────────────────────────
        List<Player> pending =
                friendDAO.getPendingRequests(me.getPlayerId());
        VBox pendingCard = makeSection(
                "Friend Requests (" + pending.size() + ")");

        if (pending.isEmpty()) {
            Text none = new Text("No pending requests.");
            none.setStyle("-fx-font-size: 13px; -fx-fill: #8B949E;");
            pendingCard.getChildren().add(none);
        } else {
            for (Player requester : pending) {
                pendingCard.getChildren().add(
                        makePendingCard(requester, me, pendingCard));
            }
        }

        // ── FRIENDS LIST ──────────────────────────────────────
        List<Player> friends = friendDAO.getFriends(me.getPlayerId());
        VBox friendsCard = makeSection(
                "My Friends (" + friends.size() + ")");

        if (friends.isEmpty()) {
            Text none = new Text(
                    "No friends yet. Search for players above!");
            none.setStyle("-fx-font-size: 13px; -fx-fill: #8B949E;");
            friendsCard.getChildren().add(none);
        } else {
            for (Player friend : friends) {
                friendsCard.getChildren().add(
                        makeFriendRow(friend, me, friendsCard));
            }
        }

        content.getChildren().addAll(
                searchCard, pendingCard, friendsCard);

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

    // ── Search result card ────────────────────────────────────
    private HBox makePlayerCard(Player found, Player me,
                                String status, String reverseStatus,
                                VBox resultArea, VBox parentCard) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #30363D;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 14;");

        // Avatar circle
        VBox avatar = makeAvatarCircle(found);

        // Player info
        VBox info = new VBox(4);
        Text name = new Text(found.getFullName());
        name.setStyle("-fx-font-size: 15px;" +
                "-fx-font-weight: bold; -fx-fill: white;");
        Text username = new Text("@" + found.getUsername());
        username.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");
        Text xp = new Text("Level " + found.getLevel() +
                "  •  " + found.getTotalXP() + " XP");
        xp.setStyle("-fx-font-size: 12px; -fx-fill: #2E75B6;");
        info.getChildren().addAll(name, username, xp);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Action button based on status
        Button actionBtn = new Button();
        actionBtn.setPrefWidth(130);
        actionBtn.setPrefHeight(38);

        if (status.equals("accepted")) {
            actionBtn.setText("✓ Friends");
            actionBtn.getStyleClass().add("btn-secondary");
            actionBtn.setDisable(true);
        } else if (status.equals("pending")) {
            actionBtn.setText("Request Sent");
            actionBtn.getStyleClass().add("btn-secondary");
            actionBtn.setDisable(true);
        } else if (reverseStatus.equals("pending")) {
            actionBtn.setText("Accept");
            actionBtn.getStyleClass().add("btn-primary");
            actionBtn.setOnAction(e -> {
                friendDAO.acceptRequest(
                        found.getPlayerId(), me.getPlayerId());
                actionBtn.setText("✓ Friends");
                actionBtn.setDisable(true);
                AnimationUtils.bounce(actionBtn);
                // Refresh screen to show in friends list
                ScreenManager.getInstance().showFriends();
            });
        } else {
            actionBtn.setText("Add Friend");
            actionBtn.getStyleClass().add("btn-primary");
            actionBtn.setOnAction(e -> {
                friendDAO.sendRequest(
                        me.getPlayerId(), found.getPlayerId());
                actionBtn.setText("Request Sent");
                actionBtn.setDisable(true);
                AnimationUtils.bounce(actionBtn);
            });
        }

        row.getChildren().addAll(avatar, info, spacer, actionBtn);
        return row;
    }

    // ── Pending request card ──────────────────────────────────
    private HBox makePendingCard(Player requester, Player me,
                                 VBox parentCard) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #E3B341;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 14;");

        VBox avatar = makeAvatarCircle(requester);

        VBox info = new VBox(4);
        Text name = new Text(requester.getFullName());
        name.setStyle("-fx-font-size: 15px;" +
                "-fx-font-weight: bold; -fx-fill: white;");
        Text username = new Text(
                "@" + requester.getUsername() +
                        " wants to be your friend");
        username.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");
        info.getChildren().addAll(name, username);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button acceptBtn = new Button("Accept");
        acceptBtn.getStyleClass().add("btn-primary");
        acceptBtn.setPrefWidth(100);
        acceptBtn.setPrefHeight(38);
        AnimationUtils.addHoverScale(acceptBtn);

        Button declineBtn = new Button("Decline");
        declineBtn.getStyleClass().add("btn-secondary");
        declineBtn.setPrefWidth(100);
        declineBtn.setPrefHeight(38);

        acceptBtn.setOnAction(e -> {
            friendDAO.acceptRequest(
                    requester.getPlayerId(), me.getPlayerId());
            AnimationUtils.bounce(acceptBtn);
            ScreenManager.getInstance().showFriends();
        });

        declineBtn.setOnAction(e -> {
            friendDAO.removeFriend(
                    requester.getPlayerId(), me.getPlayerId());
            ScreenManager.getInstance().showFriends();
        });

        HBox btns = new HBox(10, acceptBtn, declineBtn);
        row.getChildren().addAll(avatar, info, spacer, btns);
        return row;
    }

    // ── Friends list row with XP comparison ──────────────────
    private VBox makeFriendRow(Player friend, Player me,
                               VBox parentCard) {
        VBox card = new VBox(12);
        card.setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #30363D;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 16;");

        // Top row — avatar + info + remove button
        HBox topRow = new HBox(16);
        topRow.setAlignment(Pos.CENTER_LEFT);

        VBox avatar = makeAvatarCircle(friend);

        VBox info = new VBox(4);
        Text name = new Text(friend.getFullName());
        name.setStyle("-fx-font-size: 15px;" +
                "-fx-font-weight: bold; -fx-fill: white;");
        Text username = new Text("@" + friend.getUsername());
        username.setStyle(
                "-fx-font-size: 13px; -fx-fill: #8B949E;");
        Text stats = new Text(
                "Level " + friend.getLevel() +
                        "  •  " + friend.getStreak() + " day streak");
        stats.setStyle(
                "-fx-font-size: 12px; -fx-fill: #8B949E;");
        info.getChildren().addAll(name, username, stats);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeBtn = new Button("Remove");
        removeBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #F85149;" +
                        "-fx-border-color: #F85149;" +
                        "-fx-border-radius: 6;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 6 14;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 12px;");
        removeBtn.setOnAction(e -> {
            friendDAO.removeFriend(
                    me.getPlayerId(), friend.getPlayerId());
            ScreenManager.getInstance().showFriends();
        });

        topRow.getChildren().addAll(
                avatar, info, spacer, removeBtn);

        // ── XP COMPARISON BAR ─────────────────────────────────
        VBox comparison = new VBox(8);
        comparison.setAlignment(Pos.TOP_LEFT);

        Text compTitle = new Text("XP Comparison");
        compTitle.setStyle(
                "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-fill: #8B949E;");

        int myXP     = me.getTotalXP();
        int theirXP  = friend.getTotalXP();
        int maxXP    = Math.max(myXP, theirXP);
        if (maxXP == 0) maxXP = 1;

        // My bar
        HBox myRow = new HBox(10);
        myRow.setAlignment(Pos.CENTER_LEFT);
        Text myLabel = new Text("You");
        myLabel.setStyle(
                "-fx-font-size: 12px; -fx-fill: #2E75B6;" +
                        "-fx-font-weight: bold;");
        myLabel.setWrappingWidth(60);
        ProgressBar myBar = new ProgressBar(
                (double) myXP / maxXP);
        myBar.setPrefWidth(300);
        myBar.setPrefHeight(10);
        myBar.setStyle(
                "-fx-accent: #2E75B6;" +
                        "-fx-background-color: #30363D;" +
                        "-fx-background-radius: 4;");
        Text myXPText = new Text(myXP + " XP");
        myXPText.setStyle(
                "-fx-font-size: 12px; -fx-fill: #C9D1D9;");
        myRow.getChildren().addAll(myLabel, myBar, myXPText);

        // Their bar
        HBox theirRow = new HBox(10);
        theirRow.setAlignment(Pos.CENTER_LEFT);
        Text theirLabel = new Text(
                friend.getFullName().split(" ")[0]);
        theirLabel.setStyle(
                "-fx-font-size: 12px; -fx-fill: #3FB950;" +
                        "-fx-font-weight: bold;");
        theirLabel.setWrappingWidth(60);
        ProgressBar theirBar = new ProgressBar(
                (double) theirXP / maxXP);
        theirBar.setPrefWidth(300);
        theirBar.setPrefHeight(10);
        theirBar.setStyle(
                "-fx-accent: #3FB950;" +
                        "-fx-background-color: #30363D;" +
                        "-fx-background-radius: 4;");
        Text theirXPText = new Text(theirXP + " XP");
        theirXPText.setStyle(
                "-fx-font-size: 12px; -fx-fill: #C9D1D9;");
        theirRow.getChildren().addAll(
                theirLabel, theirBar, theirXPText);

        // Who's winning label
        String verdict;
        if (myXP > theirXP)
            verdict = "🏆 You're ahead by " +
                    (myXP - theirXP) + " XP!";
        else if (theirXP > myXP)
            verdict = "⚡ " + friend.getFullName().split(" ")[0] +
                    " is ahead by " + (theirXP - myXP) + " XP!";
        else
            verdict = "🤝 You're tied!";

        Text verdictText = new Text(verdict);
        verdictText.setStyle(
                "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-fill: #E3B341;");

        comparison.getChildren().addAll(
                compTitle, myRow, theirRow, verdictText);

        card.getChildren().addAll(topRow, comparison);
        return card;
    }

    // ── Avatar circle helper ──────────────────────────────────
    private VBox makeAvatarCircle(Player player) {
        VBox circle = new VBox();
        circle.setAlignment(Pos.CENTER);
        circle.setStyle(
                "-fx-background-color: #1F3864;" +
                        "-fx-background-radius: 50;" +
                        "-fx-border-color: #2E75B6;" +
                        "-fx-border-radius: 50;" +
                        "-fx-border-width: 2;" +
                        "-fx-min-width: 48px; -fx-min-height: 48px;" +
                        "-fx-max-width: 48px; -fx-max-height: 48px;");
        Text initial = new Text(String.valueOf(
                player.getFullName().charAt(0)).toUpperCase());
        initial.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold; -fx-fill: #2E75B6;");
        circle.getChildren().add(initial);
        return circle;
    }

    // ── Section card with title ───────────────────────────────
    private VBox makeSection(String title) {
        VBox card = new VBox(14);
        card.getStyleClass().add("card");
        card.setMaxWidth(580);
        card.setAlignment(Pos.TOP_LEFT);

        Text titleText = new Text(title);
        titleText.setStyle(
                "-fx-font-size: 17px; -fx-font-weight: bold;" +
                        "-fx-fill: white;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #30363D;");

        card.getChildren().addAll(titleText, sep);
        return card;
    }
}
