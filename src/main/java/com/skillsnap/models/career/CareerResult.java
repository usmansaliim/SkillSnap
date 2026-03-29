package com.skillsnap.models.career;


public class CareerResult implements Comparable<CareerResult> {
    private int    careerId;
    private String title;
    private double matchScore;    // 0-100
    private double avgSalaryPKR;
    private double marketDemand;

    public CareerResult(int careerId, String title,
                        double matchScore, double avgSalaryPKR,
                        double marketDemand) {
        this.careerId     = careerId;
        this.title        = title;
        this.matchScore   = matchScore;
        this.avgSalaryPKR = avgSalaryPKR;
        this.marketDemand = marketDemand;
    }

    public int    getCareerId()     { return careerId; }
    public String getTitle()        { return title; }
    public double getMatchScore()   { return matchScore; }
    public double getAvgSalaryPKR() { return avgSalaryPKR; }
    public double getMarketDemand() { return marketDemand; }

    // Higher score = comes first
    @Override
    public int compareTo(CareerResult other) {
        return Double.compare(other.matchScore, this.matchScore);
    }

    @Override
    public String toString() {
        return title + " — " + String.format("%.1f", matchScore) + "% match";
    }
}
