package com.skillsnap.models.career;


public class RoadmapStep {
    private int    stepNumber;
    private String title;
    private String description;
    private String duration;
    private String resourceUrl;
    private String resourceTitle;

    public RoadmapStep(int stepNumber, String title, String description,
                       String duration, String resourceUrl, String resourceTitle) {
        this.stepNumber    = stepNumber;
        this.title         = title;
        this.description   = description;
        this.duration      = duration;
        this.resourceUrl   = resourceUrl;
        this.resourceTitle = resourceTitle;
    }

    public int    getStepNumber()    { return stepNumber; }
    public String getTitle()         { return title; }
    public String getDescription()   { return description; }
    public String getDuration()      { return duration; }
    public String getResourceUrl()   { return resourceUrl; }
    public String getResourceTitle() { return resourceTitle; }
}
