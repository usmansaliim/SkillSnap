package com.skillsnap.models.game;


public class MiniGame {
    private int    gameId;
    private int    careerId;
    private String title;
    private String description;
    private String instructions;
    private String difficulty;
    private int    maxScore;
    private int    timeLimitSec;
    private int    xpReward;
    private String gameType;

    public MiniGame() {}

    public MiniGame(int gameId, int careerId, String title,
                    String description, String instructions,
                    String difficulty, int maxScore,
                    int timeLimitSec, int xpReward, String gameType) {
        this.gameId       = gameId;
        this.careerId     = careerId;
        this.title        = title;
        this.description  = description;
        this.instructions = instructions;
        this.difficulty   = difficulty;
        this.maxScore     = maxScore;
        this.timeLimitSec = timeLimitSec;
        this.xpReward     = xpReward;
        this.gameType     = gameType;
    }

    public int    getGameId()       { return gameId; }
    public int    getCareerId()     { return careerId; }
    public String getTitle()        { return title; }
    public String getDescription()  { return description; }
    public String getInstructions() { return instructions; }
    public String getDifficulty()   { return difficulty; }
    public int    getMaxScore()     { return maxScore; }
    public int    getTimeLimitSec() { return timeLimitSec; }
    public int    getXpReward()     { return xpReward; }
    public String getGameType()     { return gameType; }

    @Override
    public String toString() {
        return "MiniGame[" + title + ", " + difficulty + "]";
    }
}
