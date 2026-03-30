package com.skillsnap.engine;

import com.skillsnap.database.CareerDAO;
import com.skillsnap.database.GameDAO;
import com.skillsnap.models.career.CareerPath;
import com.skillsnap.models.career.CareerResult;
import com.skillsnap.models.career.RoadmapStep;
import com.skillsnap.models.career.SeniorAdvice;
import java.util.ArrayList;

public class RecommendationEngine {

    private GameDAO   gameDAO   = new GameDAO();
    private CareerDAO careerDAO = new CareerDAO();

    // ── Full recommendation package ───────────────────────────
    public Recommendation generate(int playerId) {
        // Get scored career list
        ArrayList<CareerResult> results =
                gameDAO.getSuggestedCareers(playerId);

        if (results.isEmpty()) return null;

        // Top match
        CareerResult top = results.get(0);

        // Fetch full career data
        CareerPath career =
                careerDAO.getCareerById(top.getCareerId());

        // Fetch roadmap
        ArrayList<RoadmapStep> roadmap =
                careerDAO.getRoadmap(top.getCareerId());

        // Fetch advice
        ArrayList<SeniorAdvice> advice =
                careerDAO.getSeniorAdvice(top.getCareerId());

        // Fetch core skills
        ArrayList<String> skills =
                careerDAO.getCoreSkills(top.getCareerId());

        return new Recommendation(
                career, top.getMatchScore(),
                results, roadmap, advice, skills);
    }

    // ── Recommendation package class ──────────────────────────
    public static class Recommendation {
        public CareerPath           topCareer;
        public double               matchScore;
        public ArrayList<CareerResult>  allResults;
        public ArrayList<RoadmapStep>   roadmap;
        public ArrayList<SeniorAdvice>  advice;
        public ArrayList<String>        coreSkills;

        public Recommendation(
                CareerPath career,
                double score,
                ArrayList<CareerResult> all,
                ArrayList<RoadmapStep> road,
                ArrayList<SeniorAdvice> adv,
                ArrayList<String> skills) {
            this.topCareer  = career;
            this.matchScore = score;
            this.allResults = all;
            this.roadmap    = road;
            this.advice     = adv;
            this.coreSkills = skills;
        }
    }
}
