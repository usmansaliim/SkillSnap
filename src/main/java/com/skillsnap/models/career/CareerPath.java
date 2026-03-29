package com.skillsnap.models.career;


public class CareerPath {
    private int    careerId;
    private String title;
    private String field;
    private String description;
    private double avgSalaryPKR;
    private double marketDemand;
    private String iconPath;

    public CareerPath() {}

    public CareerPath(int careerId, String title, String field,
                      String description, double avgSalaryPKR,
                      double marketDemand, String iconPath) {
        this.careerId     = careerId;
        this.title        = title;
        this.field        = field;
        this.description  = description;
        this.avgSalaryPKR = avgSalaryPKR;
        this.marketDemand = marketDemand;
        this.iconPath     = iconPath;
    }

    public int    getCareerId()     { return careerId; }
    public String getTitle()        { return title; }
    public String getField()        { return field; }
    public String getDescription()  { return description; }
    public double getAvgSalaryPKR() { return avgSalaryPKR; }
    public double getMarketDemand() { return marketDemand; }
    public String getIconPath()     { return iconPath; }

    @Override
    public String toString() {
        return "CareerPath[" + title + ", Demand:" + marketDemand + "]";
    }
}
