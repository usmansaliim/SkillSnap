package com.skillsnap.models.player;


public class PlayerSession {

    private static PlayerSession instance;
    private Player currentPlayer;

    private PlayerSession() {}

    public static PlayerSession getInstance() {
        if (instance == null) {
            instance = new PlayerSession();
        }
        return instance;
    }

    public void login(Player player)  { this.currentPlayer = player; }
    public void logout()              { this.currentPlayer = null; }
    public Player getCurrentPlayer()  { return currentPlayer; }
    public boolean isLoggedIn()       { return currentPlayer != null; }
}
