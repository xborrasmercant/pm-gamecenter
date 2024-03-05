package com.example.pm_gamecenter.menus;

import android.net.Uri;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardsCard {
    String username;
    int score;
    Uri picURI;
    LinearLayout.LayoutParams params;

    public LeaderboardsCard(String username, int score, Uri picURI, LinearLayout.LayoutParams params) {
        this.username = username;
        this.score = score;
        this.picURI = picURI;
        this.params = params;
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

    public LinearLayout.LayoutParams getParams() {
        return params;
    }

    public void setParams(LinearLayout.LayoutParams params) {
        this.params = params;
    }
}
