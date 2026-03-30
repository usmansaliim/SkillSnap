package com.skillsnap.database;
import com.skillsnap.models.player.*;
import com.skillsnap.database.DatabaseManager;
import java.sql.*;

public class PlayerDAO {

    private Connection conn;

    public PlayerDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    // ── REGISTER ──────────────────────────────────────────────
    public boolean register(String username, String password,
                            String fullName, String email,
                            String university, int avatarId) {
        String sql = "INSERT INTO Player (username, password_hash, full_name, " +
                "email, university, avatar_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);   // hash later
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, university);
            ps.setInt   (6, avatarId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Register failed: " + e.getMessage());
            return false;
        }
    }

    // ── LOGIN ─────────────────────────────────────────────────
    public Player login(String username, String password) {
        String sql = "SELECT * FROM Player WHERE username = ? " +
                "AND password_hash = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractPlayer(rs);
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
        return null;   // null = wrong credentials
    }

    // ── UPDATE XP ─────────────────────────────────────────────
    public void addXP(int playerId, int xpToAdd) {
        String sql = "UPDATE Player SET total_xp = total_xp + ?, " +
                "level = FLOOR((total_xp + ?) / 100) + 1 " +
                "WHERE player_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, xpToAdd);
            ps.setInt(2, xpToAdd);
            ps.setInt(3, playerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("XP update failed: " + e.getMessage());
        }
    }

    // ── UPDATE STREAK ─────────────────────────────────────────
    public void updateStreak(int playerId) {
        String sql = "UPDATE Player SET streak = CASE " +
                "WHEN DATE(last_login) = DATE(NOW() - INTERVAL 1 DAY) " +
                "THEN streak + 1 ELSE 1 END, " +
                "last_login = NOW() WHERE player_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Streak update failed: " + e.getMessage());
        }
    }

    // ── GET FRESH PLAYER DATA ─────────────────────────────────
    public Player getPlayerById(int playerId) {
        String sql = "SELECT * FROM Player WHERE player_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extractPlayer(rs);
        } catch (SQLException e) {
            System.out.println("Fetch failed: " + e.getMessage());
        }
        return null;
    }

    // ── CHECK USERNAME TAKEN ──────────────────────────────────
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM Player WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    // ── HELPER — build Player from ResultSet row ──────────────
    private Player extractPlayer(ResultSet rs) throws SQLException {
        return new Player(
                rs.getInt   ("player_id"),
                rs.getString("username"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("university"),
                rs.getInt   ("avatar_id"),
                rs.getInt   ("total_xp"),
                rs.getInt   ("level"),
                rs.getInt   ("streak")
        );
    }
    // ── UPDATE USERNAME ───────────────────────────────────────
    public boolean updateUsername(int playerId, String newUsername) {
        String sql = "UPDATE Player SET username = ? WHERE player_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUsername);
            ps.setInt(2, playerId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Username update failed: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE PASSWORD ───────────────────────────────────────
    public boolean updatePassword(int playerId,
                                  String currentPassword,
                                  String newPassword) {
        // First verify current password is correct
        String checkSql = "SELECT 1 FROM Player " +
                "WHERE player_id = ? AND password_hash = ?";
        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, playerId);
            ps.setString(2, currentPassword);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return false; // wrong current password

            // Now update
            String updateSql = "UPDATE Player SET password_hash = ? " +
                    "WHERE player_id = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(updateSql)) {
                ps2.setString(1, newPassword);
                ps2.setInt(2, playerId);
                ps2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Password update failed: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE AVATAR ─────────────────────────────────────────
    public boolean updateAvatar(int playerId, int avatarId) {
        String sql = "UPDATE Player SET avatar_id = ? WHERE player_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, avatarId);
            ps.setInt(2, playerId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Avatar update failed: " + e.getMessage());
            return false;
        }
    }

}