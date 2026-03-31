package com.skillsnap.database;

import com.skillsnap.models.player.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {

    private Connection conn;

    public FriendDAO() {
        conn = DatabaseManager.getInstance().getConnection();
    }

    // ── Send friend request ───────────────────────────────────
    public void sendRequest(int fromId, int toId) {
        String sql =
                "INSERT IGNORE INTO Friendship " +
                        "(player_id, friend_id, status) " +
                        "VALUES (?, ?, 'pending')";
        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {
            ps.setInt(1, fromId);
            ps.setInt(2, toId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("sendRequest failed: "
                    + e.getMessage());
        }
    }

    // ── Accept friend request ─────────────────────────────────
    public void acceptRequest(int fromId, int toId) {
        String sql =
                "UPDATE Friendship SET status = 'accepted' " +
                        "WHERE player_id = ? AND friend_id = ?";
        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {
            ps.setInt(1, fromId);
            ps.setInt(2, toId);
            ps.executeUpdate();

            // Insert reverse relationship
            String reverse =
                    "INSERT IGNORE INTO Friendship " +
                            "(player_id, friend_id, status) " +
                            "VALUES (?, ?, 'accepted')";
            try (PreparedStatement ps2 =
                         conn.prepareStatement(reverse)) {
                ps2.setInt(1, toId);
                ps2.setInt(2, fromId);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("acceptRequest failed: "
                    + e.getMessage());
        }
    }

    // ── Remove friend ─────────────────────────────────────────
    public void removeFriend(int playerId, int friendId) {
        String sql =
                "DELETE FROM Friendship " +
                        "WHERE (player_id = ? AND friend_id = ?) " +
                        "OR (player_id = ? AND friend_id = ?)";
        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setInt(2, friendId);
            ps.setInt(3, friendId);
            ps.setInt(4, playerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("removeFriend failed: "
                    + e.getMessage());
        }
    }

    // ── Get relationship status ───────────────────────────────
    public String getStatus(int fromId, int toId) {
        String sql =
                "SELECT status FROM Friendship " +
                        "WHERE player_id = ? AND friend_id = ?";
        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {
            ps.setInt(1, fromId);
            ps.setInt(2, toId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("status");
        } catch (SQLException e) {
            System.out.println("getStatus failed: "
                    + e.getMessage());
        }
        return "none";
    }

    // ── Get accepted friends list ─────────────────────────────
    public List<Player> getFriends(int playerId) {
        List<Player> friends = new ArrayList<>();
        String sql =
                "SELECT p.* FROM Player p " +
                        "JOIN Friendship f ON p.player_id = f.friend_id " +
                        "WHERE f.player_id = ? " +
                        "AND f.status = 'accepted'";
        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                friends.add(extractPlayer(rs));
        } catch (SQLException e) {
            System.out.println("getFriends failed: "
                    + e.getMessage());
        }
        return friends;
    }

    // ── Get pending incoming requests ─────────────────────────
    public List<Player> getPendingRequests(int playerId) {
        List<Player> requesters = new ArrayList<>();
        String sql =
                "SELECT p.* FROM Player p " +
                        "JOIN Friendship f ON p.player_id = f.player_id " +
                        "WHERE f.friend_id = ? " +
                        "AND f.status = 'pending'";
        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                requesters.add(extractPlayer(rs));
        } catch (SQLException e) {
            System.out.println("getPendingRequests failed: "
                    + e.getMessage());
        }
        return requesters;
    }

    // ── Search player by username ─────────────────────────────
    public Player searchByUsername(String username,
                                   int excludeId) {
        String sql =
                "SELECT * FROM Player " +
                        "WHERE username = ? " +
                        "AND player_id != ?";
        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extractPlayer(rs);
        } catch (SQLException e) {
            System.out.println("searchByUsername failed: "
                    + e.getMessage());
        }
        return null;
    }

    // ── Helper ────────────────────────────────────────────────
    private Player extractPlayer(ResultSet rs)
            throws SQLException {
        return new Player(
                rs.getInt("player_id"),
                rs.getString("username"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("university"),
                rs.getInt("avatar_id"),
                rs.getInt("total_xp"),
                rs.getInt("level"),
                rs.getInt("streak")
        );
    }
}