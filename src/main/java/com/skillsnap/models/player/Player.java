package com.skillsnap.models.player;


public class Player {
    private int playerId;
    private String username;
    private String fullName;
    private String email;
    private String university;
    private int avatarId;
    private int totalXP;
    private int level;
    private int streak;

    public Player() {}

    public Player(int playerId, String username, String fullName,
                  String email, String university, int avatarId,
                  int totalXP, int level, int streak) {
        this.playerId   = playerId;
        this.username   = username;
        this.fullName   = fullName;
        this.email      = email;
        this.university = university;
        this.avatarId   = avatarId;
        this.totalXP    = totalXP;
        this.level      = level;
        this.streak     = streak;
    }

    // Getters
    public int    getPlayerId()   { return playerId; }
    public String getUsername()   { return username; }
    public String getFullName()   { return fullName; }
    public String getEmail()      { return email; }
    public String getUniversity() { return university; }
    public int    getAvatarId()   { return avatarId; }
    public int    getTotalXP()    { return totalXP; }
    public int    getLevel()      { return level; }
    public int    getStreak()     { return streak; }

    // Setters
    public void setTotalXP(int xp)    { this.totalXP = xp; }
    public void setLevel(int level)   { this.level = level; }
    public void setStreak(int streak) { this.streak = streak; }

    @Override
    public String toString() {
        return "Player[" + username + ", Level " + level + ", XP:" + totalXP + "]";
    }
}
