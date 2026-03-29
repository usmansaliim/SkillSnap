package com.skillsnap.models.game;


public class GameResult {
    private int    gameId;
    private int    score;
    private int    maxScore;
    private int    timeTakenSec;
    private int    xpEarned;
    private String message;

    public GameResult(int gameId, int score, int maxScore,
                      int timeTakenSec, int xpEarned, String message) {
        this.gameId      = gameId;
        this.score       = score;
        this.maxScore    = maxScore;
        this.timeTakenSec= timeTakenSec;
        this.xpEarned    = xpEarned;
        this.message     = message;
    }

    public int    getGameId()       { return gameId; }
    public int    getScore()        { return score; }
    public int    getMaxScore()     { return maxScore; }
    public int    getTimeTakenSec() { return timeTakenSec; }
    public int    getXpEarned()     { return xpEarned; }
    public String getMessage()      { return message; }

    public double getPercentage() {
        return (score * 100.0) / maxScore;
    }
}
