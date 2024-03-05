package com.example.pm_gamecenter.menus;

import android.net.Uri;

public class LeaderboardsCard {
    String username;
    int score;
    Uri picURI;

    public LeaderboardsCard(String username, int score, Uri picURI) {
        this.username = username;
        this.score = score;
        this.picURI = picURI;
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

    public Uri getPicURI() {
        return picURI;
    }

    public void setPicURI(Uri picURI) {
        this.picURI = picURI;
    }
}
