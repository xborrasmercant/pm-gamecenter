package com.example.pm_gamecenter;

public class LeaderboardsCard {
    String profilePicturePath, username;
    int score;

    public LeaderboardsCard(String profilePicturePath, String username, int score) {
        this.profilePicturePath = profilePicturePath;
        this.username = username;
        this.score = score;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
