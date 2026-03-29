package com.skillsnap.database;
import com.skillsnap.models.career.CareerResult;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class GameDAO {

    private Connection conn;

    public GameDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    // ── SAVE GAME SESSION ─────────────────────────────────────
    // Called every time a player finishes a game
    public boolean saveSession(int playerId, int gameId,
                               int score, int maxScore,
                               int timeTakenSec) {
        String sql = "INSERT INTO GameSession " +
                "(player_id, game_id, score, max_score, " +
                "time_taken_sec, completed, xp_earned) " +
                "VALUES (?, ?, ?, ?, ?, TRUE, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int xpEarned = calculateXP(score, maxScore);
            ps.setInt(1, playerId);
            ps.setInt(2, gameId);
            ps.setInt(3, score);
            ps.setInt(4, maxScore);
            ps.setInt(5, timeTakenSec);
            ps.setInt(6, xpEarned);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("saveSession failed: " + e.getMessage());
            return false;
        }
    }

    // ── THE BIG ONE — CAREER SUGGESTION QUERY ─────────────────
    // Runs after player has played enough games
    // Returns careers sorted by match score — best match first
    public ArrayList<CareerResult> getSuggestedCareers(int playerId) {
        ArrayList<CareerResult> results = new ArrayList<>();
        String sql =
                "SELECT cp.career_id, cp.title, cp.avg_salary_pkr, " +
                        "cp.market_demand, " +
                        "AVG(gs.score * 100.0 / gs.max_score) AS match_score, " +
                        "COUNT(gs.session_id) AS games_played " +
                        "FROM CareerPath cp " +
                        "JOIN MiniGame mg ON cp.career_id = mg.career_id " +
                        "JOIN GameSession gs ON mg.game_id = gs.game_id " +
                        "WHERE gs.player_id = ? AND gs.completed = TRUE " +
                        "GROUP BY cp.career_id, cp.title, " +
                        "cp.avg_salary_pkr, cp.market_demand " +
                        "HAVING games_played >= 1 " +
                        "ORDER BY match_score DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(new CareerResult(
                        rs.getInt   ("career_id"),
                        rs.getString("title"),
                        rs.getDouble("match_score"),
                        rs.getDouble("avg_salary_pkr"),
                        rs.getDouble("market_demand")
                ));
            }
        } catch (SQLException e) {
            System.out.println("getSuggestedCareers failed: " + e.getMessage());
        }
        Collections.sort(results);  // uses CareerResult.compareTo()
        return results;
    }

    // ── GET BEST SCORE FOR ONE GAME ───────────────────────────
    public int getBestScore(int playerId, int gameId) {
        String sql = "SELECT MAX(score) AS best FROM GameSession " +
                "WHERE player_id = ? AND game_id = ? " +
                "AND completed = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setInt(2, gameId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("best");
        } catch (SQLException e) {
            System.out.println("getBestScore failed: " + e.getMessage());
        }
        return 0;
    }

    // ── GET TOTAL GAMES PLAYED ────────────────────────────────
    public int getTotalGamesPlayed(int playerId) {
        String sql = "SELECT COUNT(*) AS total FROM GameSession " +
                "WHERE player_id = ? AND completed = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.out.println("getTotalGamesPlayed failed: " + e.getMessage());
        }
        return 0;
    }

    // ── GET DISTINCT CAREERS PLAYED ───────────────────────────
    // Used for Explorer badge — how many different careers tried
    public int getDistinctCareersTried(int playerId) {
        String sql = "SELECT COUNT(DISTINCT mg.career_id) AS total " +
                "FROM GameSession gs " +
                "JOIN MiniGame mg ON gs.game_id = mg.game_id " +
                "WHERE gs.player_id = ? AND gs.completed = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.out.println("getDistinctCareersTried failed: " + e.getMessage());
        }
        return 0;
    }

    // ── HAS PLAYER PLAYED A SPECIFIC GAME ────────────────────
    public boolean hasPlayed(int playerId, int gameId) {
        String sql = "SELECT 1 FROM GameSession " +
                "WHERE player_id = ? AND game_id = ? " +
                "AND completed = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setInt(2, gameId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    // ── SAVE CAREER SUGGESTION TO DB ─────────────────────────
    public void saveSuggestion(int playerId, int careerId,
                               double matchScore, int rank) {
        String sql = "INSERT INTO CareerSuggestion " +
                "(player_id, career_id, match_score, rank_position) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, playerId);
            ps.setInt   (2, careerId);
            ps.setDouble(3, matchScore);
            ps.setInt   (4, rank);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("saveSuggestion failed: " + e.getMessage());
        }
    }

    // ── PRIVATE — XP CALCULATION ──────────────────────────────
    // Score % determines XP earned from that game's reward pool
    private int calculateXP(int score, int maxScore) {
        double percentage = (score * 100.0) / maxScore;
        if      (percentage >= 90) return 100;
        else if (percentage >= 70) return 75;
        else if (percentage >= 50) return 50;
        else                       return 25;
    }
}
