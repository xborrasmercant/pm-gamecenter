package com.example.pm_gamecenter;

public class User {
    private String username, password, profilePicturePath;
    private int highScore_2048, highScore_Senku;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.profilePicturePath = null;
        this.highScore_2048 = 0;
        this.highScore_Senku = 0;
    }

    public User() {
        // Empty constructor
    }

// Getters and setters


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getProfilePicturePath() {
        return profilePicturePath;
    }
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
    public int getHighScore_2048() {
        return highScore_2048;
    }
    public void setHighScore_2048(int highScore_2048) {
        this.highScore_2048 = highScore_2048;
    }
    public int getHighScore_Senku() {
        return highScore_Senku;
    }
    public void setHighScore_Senku(int highScore_Senku) {
        this.highScore_Senku = highScore_Senku;
    }
}
