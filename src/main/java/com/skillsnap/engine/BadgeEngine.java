package com.skillsnap.engine;

import com.skillsnap.database.DatabaseManager;
import com.skillsnap.database.GameDAO;
import com.skillsnap.models.player.Player;
import java.sql.*;
import java.util.ArrayList;

public class BadgeEngine {

    private GameDAO gameDAO = new GameDAO();

    // ── Main method — call this after every game ──────────────
    // Returns list of newly earned badge names
    public ArrayList<String> checkAndAward(Player player,
                                           int lastScore,
                                           int lastMaxScore) {
        ArrayList<String> newBadges = new ArrayList<>();

        int playerId      = player.getPlayerId();
        int totalGames    = gameDAO.getTotalGamesPlayed(playerId);
        int careersTried  = gameDAO.getDistinctCareersTried(playerId);
        int streak        = player.getStreak();
        double scorePct   = (lastScore * 100.0) / lastMaxScore;

        // Check each badge condition
        if (totalGames >= 1)
            tryAward(playerId, "First Step", newBadges);
        if (streak >= 3)
            tryAward(playerId, "On Fire", newBadges);
        if (streak >= 7)
            tryAward(playerId, "Dedicated", newBadges);
        if (streak >= 30)
            tryAward(playerId, "Unstoppable", newBadges);
        if (careersTried >= 3)
            tryAward(playerId, "Explorer", newBadges);
        if (careersTried >= 6)
            tryAward(playerId, "Adventurer", newBadges);
        if (scorePct >= 90)
            tryAward(playerId, "Sharp Shooter", newBadges);
        if (scorePct >= 100)
            tryAward(playerId, "Perfectionist", newBadges);
        if (totalGames >= 25)
            tryAward(playerId, "Grinder", newBadges);

        return newBadges;
    }

    // ── Try to award badge — skip if already earned ───────────
    private void tryAward(int playerId,
                          String badgeName,
                          ArrayList<String> newBadges) {
        if (alreadyEarned(playerId, badgeName)) return;

        int badgeId = getBadgeId(badgeName);
        if (badgeId == -1) return;

        String sql =
                "INSERT INTO PlayerBadge (player_id, badge_id) " +
                        "VALUES (?, ?)";
        try {
            Connection conn =
                    DatabaseManager.getInstance().getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(sql);
            ps.setInt(1, playerId);
            ps.setInt(2, badgeId);
            ps.executeUpdate();
            newBadges.add(badgeName);

            // Award the XP for this badge
            awardBadgeXP(playerId, badgeId);

        } catch (SQLException e) {
            System.out.println(
                    "Badge award failed: " + e.getMessage());
        }
    }

    // ── Check if player already has this badge ────────────────
    private boolean alreadyEarned(int playerId,
                                  String badgeName) {
        String sql =
                "SELECT 1 FROM PlayerBadge pb " +
                        "JOIN Badge b ON pb.badge_id = b.badge_id " +
                        "WHERE pb.player_id = ? AND b.name = ?";
        try {
            Connection conn =
                    DatabaseManager.getInstance().getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(sql);
            ps.setInt(1, playerId);
            ps.setString(2, badgeName);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    // ── Get badge_id by name ──────────────────────────────────
    private int getBadgeId(String badgeName) {
        String sql =
                "SELECT badge_id FROM Badge WHERE name = ?";
        try {
            Connection conn =
                    DatabaseManager.getInstance().getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(sql);
            ps.setString(1, badgeName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("badge_id");
        } catch (SQLException e) {
            System.out.println(
                    "getBadgeId failed: " + e.getMessage());
        }
        return -1;
    }

    // ── Award XP for earning a badge ──────────────────────────
    private void awardBadgeXP(int playerId, int badgeId) {
        String sql =
                "UPDATE Player SET total_xp = total_xp + " +
                        "(SELECT xp_reward FROM Badge WHERE badge_id = ?) " +
                        "WHERE player_id = ?";
        try {
            Connection conn =
                    DatabaseManager.getInstance().getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(sql);
            ps.setInt(1, badgeId);
            ps.setInt(2, playerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(
                    "Badge XP award failed: " + e.getMessage());
        }
    }

    // ── Get all earned badges for a player ────────────────────
    public ArrayList<String[]> getEarnedBadges(int playerId) {
        ArrayList<String[]> badges = new ArrayList<>();
        String sql =
                "SELECT b.name, b.description, b.xp_reward, " +
                        "pb.earned_at " +
                        "FROM Badge b " +
                        "JOIN PlayerBadge pb ON b.badge_id = pb.badge_id " +
                        "WHERE pb.player_id = ? " +
                        "ORDER BY pb.earned_at DESC";
        try {
            Connection conn =
                    DatabaseManager.getInstance().getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(sql);
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                badges.add(new String[]{
                        rs.getString("name"),
                        rs.getString("description"),
                        String.valueOf(rs.getInt("xp_reward")),
                        rs.getString("earned_at")
                });
            }
        } catch (SQLException e) {
            System.out.println(
                    "getEarnedBadges failed: " + e.getMessage());
        }
        return badges;
    }
}
