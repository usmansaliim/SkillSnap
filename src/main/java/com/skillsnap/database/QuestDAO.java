package com.skillsnap.database;

import com.skillsnap.models.quest.Quest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestDAO {

    private Connection conn;

    public QuestDAO() {
        conn = DatabaseManager.getInstance().getConnection();
    }

    // ── Get all active daily quests with player progress ─────
    public List<Quest> getDailyQuestsForPlayer(int playerId) {
        List<Quest> quests = new ArrayList<>();
        String sql =
                "SELECT q.quest_id, q.title, q.description, " +
                        "q.condition_value, q.xp_reward, " +
                        "COALESCE(pq.progress, 0) AS progress, " +
                        "COALESCE(pq.completed, FALSE) AS completed " +
                        "FROM Quest q " +
                        "LEFT JOIN PlayerQuest pq " +
                        "  ON q.quest_id = pq.quest_id " +
                        "  AND pq.player_id = ? " +
                        "WHERE q.quest_type = 'daily' AND q.is_active = TRUE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Quest q = new Quest();
                q.setQuestId(rs.getInt("quest_id"));
                q.setTitle(rs.getString("title"));
                q.setDescription(rs.getString("description"));
                q.setConditionValue(rs.getInt("condition_value"));
                q.setXpReward(rs.getInt("xp_reward"));
                q.setProgress(rs.getInt("progress"));
                q.setCompleted(rs.getBoolean("completed"));
                quests.add(q);
            }
        } catch (SQLException e) {
            System.out.println("getDailyQuests error: " + e.getMessage());
        }
        return quests;
    }

    // ── Assign daily quests to player if not yet assigned ────
    public void assignDailyQuestsIfNeeded(int playerId) {
        String checkSql =
                "SELECT COUNT(*) FROM PlayerQuest pq " +
                        "JOIN Quest q ON pq.quest_id = q.quest_id " +
                        "WHERE pq.player_id = ? AND q.quest_type = 'daily' " +
                        "AND DATE(pq.assigned_at) = CURDATE()";

        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return; // already assigned today

            // Assign all active daily quests
            String insertSql =
                    "INSERT IGNORE INTO PlayerQuest (player_id, quest_id) " +
                            "SELECT ?, quest_id FROM Quest " +
                            "WHERE quest_type = 'daily' AND is_active = TRUE";
            try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                ins.setInt(1, playerId);
                ins.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("assignDailyQuests error: " + e.getMessage());
        }
    }

    // ── Update quest progress after a game ───────────────────
    public void updateQuestProgress(int playerId,
                                    int gamesPlayedToday,
                                    int lastScorePct) {
        // Update play_games quests
        String playGamesSql =
                "UPDATE PlayerQuest pq " +
                        "JOIN Quest q ON pq.quest_id = q.quest_id " +
                        "SET pq.progress = ?, " +
                        "    pq.completed = (? >= q.condition_value) " +
                        "WHERE pq.player_id = ? " +
                        "AND q.condition_type = 'play_games' " +
                        "AND q.quest_type = 'daily'";

        // Update score_above quests
        String scoreSql =
                "UPDATE PlayerQuest pq " +
                        "JOIN Quest q ON pq.quest_id = q.quest_id " +
                        "SET pq.progress = ?, " +
                        "    pq.completed = (? >= q.condition_value), " +
                        "    pq.completed_at = IF(? >= q.condition_value, NOW(), NULL) " +
                        "WHERE pq.player_id = ? " +
                        "AND q.condition_type = 'score_above' " +
                        "AND q.quest_type = 'daily' " +
                        "AND pq.completed = FALSE";

        try (PreparedStatement ps1 = conn.prepareStatement(playGamesSql);
             PreparedStatement ps2 = conn.prepareStatement(scoreSql)) {

            ps1.setInt(1, gamesPlayedToday);
            ps1.setInt(2, gamesPlayedToday);
            ps1.setInt(3, playerId);
            ps1.executeUpdate();

            ps2.setInt(1, lastScorePct);
            ps2.setInt(2, lastScorePct);
            ps2.setInt(3, lastScorePct);
            ps2.setInt(4, playerId);
            ps2.executeUpdate();

        } catch (SQLException e) {
            System.out.println("updateQuestProgress error: " + e.getMessage());
        }
    }

    // ── Count games played today by player ───────────────────
    public int getGamesPlayedToday(int playerId) {
        String sql =
                "SELECT COUNT(*) FROM GameSession " +
                        "WHERE player_id = ? " +
                        "AND DATE(played_at) = CURDATE() " +
                        "AND completed = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("getGamesPlayedToday error: " + e.getMessage());
        }
        return 0;
    }
}