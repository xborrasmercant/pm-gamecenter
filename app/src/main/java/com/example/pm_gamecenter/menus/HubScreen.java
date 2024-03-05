package com.example.pm_gamecenter.menus;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.pm_gamecenter.R;
import com.example.pm_gamecenter.game2048.Game2048Screen;
import com.example.pm_gamecenter.gameSenku.GameSenkuScreen;
import com.example.pm_gamecenter.utilities.User;
import com.example.pm_gamecenter.utilities.UserManager;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

public class HubScreen extends AppCompatActivity {

    UserManager userManager = UserManager.getInstance();
    TextView usernameView, game1Title, game2Title;
    ImageView profilePic, gamePic1, gamePic2, leaderboardsIcon, settingsIcon;
    LinearLayout gameCard1, gameCard2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_hub);
        Log.i("ACTIVE_USER_2048_HIGHSCORE", String.valueOf(userManager.getActiveUser().getHighScore_2048()));

        findViews();
        editViewsAttributes();
        setClickListeners();
    }

    public void findViews() {
        profilePic = findViewById(R.id.navbar_logo);
        usernameView = findViewById(R.id.navbar_username);
        leaderboardsIcon = findViewById(R.id.navbar_leaderboards);
        settingsIcon = findViewById(R.id.navbar_settings);
        gamePic1 = findViewById(R.id.game1_bitmap);
        gamePic2 = findViewById(R.id.game2_bitmap);
        gameCard1 = findViewById(R.id.game1_main);
        gameCard2 = findViewById(R.id.game2_main);
        game1Title = findViewById(R.id.game1_title);
        game2Title = findViewById(R.id.game2_title);
    }

    public void editViewsAttributes() {
        int gamePicWidth = getDisplayWidth()*55/100;
        int profilePicWidth = getDisplayWidth()*10/100;
        int usernameViewSize = (int) (getDisplayWidth()*2/100);
        int gameTitleSize = (int) (getDisplayWidth()*2.5/100);
        Typeface titleFont = ResourcesCompat.getFont(this, R.font.verdana_bold);

        setProfilePicture(profilePic, userManager.getActiveUser().getUsername());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(profilePicWidth, profilePicWidth);
        params.setMargins(16,8,8,8);
        profilePic.setLayoutParams(params);

        usernameView.setText(userManager.getActiveUser().getUsername());
        usernameView.setTextSize(usernameViewSize);

        params = new LinearLayout.LayoutParams(gamePicWidth, gamePicWidth);
        gamePic1.setLayoutParams(params);
        gamePic2.setLayoutParams(params);

        game1Title.setTextSize(gameTitleSize);
        game2Title.setTextSize(gameTitleSize);
        game1Title.setTypeface(titleFont);
        game2Title.setTypeface(titleFont);
    }

    public void setClickListeners() {
        leaderboardsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK", "LEADERBOARDS");
                sortUsersAlphabetically();
                startActivity(new Intent(HubScreen.this, LeaderboardsScreen.class));
            }
        });

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK", "SETTINGS");
                startActivity(new Intent(HubScreen.this, SettingsScreen.class));
            }
        });

        gameCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK", "GAME 2048");
                startActivity(new Intent(HubScreen.this, Game2048Screen.class));

            }
        });

        gameCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK", "GAME SENKU");
                startActivity(new Intent(HubScreen.this, GameSenkuScreen.class));

            }
        });
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

    private void setProfilePicture(ImageView imageView, String username) {
        Uri imageUri = getProfilePictureUri(username);
        imageView.setImageURI(imageUri);
    }

    public int getDisplayWidth(){
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public void sortUsersAlphabetically() {
        Collections.sort(userManager.getUsers(), new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u1.getUsername().compareToIgnoreCase(u2.getUsername());
            }
        });
    }
}
