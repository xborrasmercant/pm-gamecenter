package com.example.pm_gamecenter.menus;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm_gamecenter.R;
import com.example.pm_gamecenter.utilities.User;
import com.example.pm_gamecenter.utilities.UserManager;

import java.util.ArrayList;

public class LeaderboardsScreen extends AppCompatActivity {

    private UserManager userManager = UserManager.getInstance();
    private RecyclerView recyclerView1, recyclerView2;
    private ArrayList<LeaderboardsCard> leaderboardCards1, leaderboardCards2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_leaderboards);
        findViews();
        fillLeaderboards();
        setUpAdapters();
    }

    public void findViews() {
        recyclerView1 = findViewById(R.id.leaderboard_game1_recyclerview);
        recyclerView2 = findViewById(R.id.leaderboard_game2_recyclerview);
        leaderboardCards1 = new ArrayList<>();
        leaderboardCards2 = new ArrayList<>();
    }

    public void fillLeaderboards() {
        for(User u : userManager.getUsers()) {
            String profilePicturePath = u.getProfilePicturePath();
            String username = u.getUsername();
            int score2048 = u.getHighScore_2048();
            int scoreSenku = u.getHighScore_Senku();

            leaderboardCards1.add(new LeaderboardsCard(profilePicturePath, username, score2048));
            leaderboardCards2.add(new LeaderboardsCard(profilePicturePath, username, scoreSenku));
        }
    }

    public void setUpAdapters() {
        LeaderboardsRVAdapter adapter1 = new LeaderboardsRVAdapter(this, leaderboardCards1);
        recyclerView1.setAdapter(adapter1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        LeaderboardsRVAdapter adapter2 = new LeaderboardsRVAdapter(this, leaderboardCards2);
        recyclerView2.setAdapter(adapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

    }

    public int getDisplayWidth(){
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public int getDisplayHeight(){
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
}
