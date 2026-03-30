package com.skillsnap.database;
import com.skillsnap.models.career.*;
import com.skillsnap.models.game.*;

import java.sql.*;
import java.util.ArrayList;

public class CareerDAO {

    private Connection conn;

    public CareerDAO() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    // ── GET ALL ACTIVE CAREERS ────────────────────────────────
    public ArrayList<CareerPath> getAllCareers() {
        ArrayList<CareerPath> careers = new ArrayList<>();
        String sql = "SELECT * FROM CareerPath WHERE is_active = TRUE " +
                "ORDER BY career_id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                careers.add(extractCareer(rs));
            }
        } catch (SQLException e) {
            System.out.println("getAllCareers failed: " + e.getMessage());
        }
        return careers;
    }

    // ── GET ONE CAREER BY ID ──────────────────────────────────
    public CareerPath getCareerById(int careerId) {
        String sql = "SELECT * FROM CareerPath WHERE career_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, careerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extractCareer(rs);
        } catch (SQLException e) {
            System.out.println("getCareerById failed: " + e.getMessage());
        }
        return null;
    }

    // ── GET CAREERS BY FIELD ──────────────────────────────────
    // Useful for filtering — Tech, Healthcare, Engineering etc
    public ArrayList<CareerPath> getCareersByField(String field) {
        ArrayList<CareerPath> careers = new ArrayList<>();
        String sql = "SELECT * FROM CareerPath WHERE field = ? " +
                "AND is_active = TRUE ORDER BY market_demand DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, field);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) careers.add(extractCareer(rs));
        } catch (SQLException e) {
            System.out.println("getCareersByField failed: " + e.getMessage());
        }
        return careers;
    }

    // ── GET MINI GAMES FOR A CAREER ───────────────────────────
    public ArrayList<MiniGame> getGamesForCareer(int careerId) {
        ArrayList<MiniGame> games = new ArrayList<>();
        String sql = "SELECT * FROM MiniGame WHERE career_id = ? " +
                "AND is_active = TRUE ORDER BY difficulty";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, careerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                games.add(new MiniGame(
                        rs.getInt   ("game_id"),
                        rs.getInt   ("career_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        "",
                        rs.getString("difficulty"),
                        rs.getInt   ("max_score"),
                        rs.getInt   ("time_limit_sec"),
                        rs.getInt   ("xp_reward"),
                        rs.getString("game_type")
                ));
            }
        } catch (SQLException e) {
            System.out.println("getGamesForCareer failed: " + e.getMessage());
        }
        return games;
    }

    // ── GET ROADMAP FOR A CAREER ──────────────────────────────
    public ArrayList<RoadmapStep> getRoadmap(int careerId) {
        ArrayList<RoadmapStep> steps = new ArrayList<>();
        String sql = "SELECT * FROM RoadmapStep WHERE career_id = ? " +
                "ORDER BY step_number";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, careerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                steps.add(new RoadmapStep(
                        rs.getInt   ("step_number"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("duration"),
                        rs.getString("resource_url"),
                        rs.getString("resource_title")
                ));
            }
        } catch (SQLException e) {
            System.out.println("getRoadmap failed: " + e.getMessage());
        }
        return steps;
    }

    // ── GET SENIOR ADVICE FOR A CAREER ────────────────────────
    public ArrayList<SeniorAdvice> getSeniorAdvice(int careerId) {
        ArrayList<SeniorAdvice> adviceList = new ArrayList<>();
        String sql = "SELECT * FROM SeniorAdvice WHERE career_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, careerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                adviceList.add(new SeniorAdvice(
                        rs.getString("senior_name"),
                        rs.getString("current_role"),
                        rs.getString("company"),
                        rs.getString("university"),
                        rs.getInt   ("years_exp"),
                        rs.getString("advice_text")
                ));
            }
        } catch (SQLException e) {
            System.out.println("getSeniorAdvice failed: " + e.getMessage());
        }
        return adviceList;
    }

    // ── GET SKILLS FOR A CAREER ───────────────────────────────
    public ArrayList<String> getCoreSkills(int careerId) {
        ArrayList<String> skills = new ArrayList<>();
        String sql = "SELECT s.name FROM Skill s " +
                "JOIN CareerSkill cs ON s.skill_id = cs.skill_id " +
                "WHERE cs.career_id = ? AND cs.is_core = TRUE " +
                "ORDER BY cs.importance DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, careerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) skills.add(rs.getString("name"));
        } catch (SQLException e) {
            System.out.println("getCoreSkills failed: " + e.getMessage());
        }
        return skills;
    }

    // ── HELPER — build CareerPath from ResultSet row ──────────
    private CareerPath extractCareer(ResultSet rs) throws SQLException {
        return new CareerPath(
                rs.getInt   ("career_id"),
                rs.getString("title"),
                rs.getString("field"),
                rs.getString("description"),
                rs.getDouble("avg_salary_pkr"),
                rs.getDouble("market_demand"),
                rs.getString("icon_path")
        );
    }
}