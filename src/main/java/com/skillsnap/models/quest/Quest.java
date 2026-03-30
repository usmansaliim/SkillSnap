package com.skillsnap.models.quest;

public class Quest {

    private int questId;
    private String title;
    private String description;
    private int conditionValue;
    private int xpReward;
    private int progress;
    private boolean completed;

    // ── Getters ───────────────────────────────────────────────
    public int getQuestId()         { return questId; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }
    public int getConditionValue()  { return conditionValue; }
    public int getXpReward()        { return xpReward; }
    public int getProgress()        { return progress; }
    public boolean isCompleted()    { return completed; }

    // ── Setters ───────────────────────────────────────────────
    public void setQuestId(int questId)             { this.questId = questId; }
    public void setTitle(String title)              { this.title = title; }
    public void setDescription(String description)  { this.description = description; }
    public void setConditionValue(int v)            { this.conditionValue = v; }
    public void setXpReward(int xpReward)           { this.xpReward = xpReward; }
    public void setProgress(int progress)           { this.progress = progress; }
    public void setCompleted(boolean completed)     { this.completed = completed; }
}
