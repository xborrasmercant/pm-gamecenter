package com.example.pm_gamecenter.menus;

import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm_gamecenter.R;
import com.example.pm_gamecenter.utilities.User;
import com.example.pm_gamecenter.utilities.UserManager;

import java.io.File;
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
            Uri picUri = getProfilePictureUri(username);

            leaderboardCards1.add(new LeaderboardsCard(username, score2048, picUri));
            leaderboardCards2.add(new LeaderboardsCard(username, scoreSenku, picUri));
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

    private Uri getProfilePictureUri(String username) {
        File pictureDir = new File(getFilesDir(), "ProfilePictures");
        File profilePicture = new File(pictureDir, username + ".png");
        if (profilePicture.exists()) {
            return Uri.fromFile(profilePicture);
        } else {
            // If no profile picture, return the URI of the default drawable
            return Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.bmp_avatar_default);
        }
    }

    // Call this method when you want to set the ImageView with the user's profile picture
    private void setProfilePicture(ImageView imageView, String username) {
        Uri imageUri = getProfilePictureUri(username);
        imageView.setImageURI(imageUri);
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
