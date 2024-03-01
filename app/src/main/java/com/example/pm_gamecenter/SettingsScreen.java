package com.example.pm_gamecenter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class SettingsScreen extends AppCompatActivity {

    private UserManager userManager = UserManager.getInstance();
    private ImageView profilePicture, editUsernamePicture, editPasswordPicture;
    private TextView usernameLabel, usernameField, passwordLabel, passwordField;
    private Button resetScoresBtn, deleteAccountBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_settings);
        findViews();
        editViewsAttributes();
        setClickListeners();
    }

    public void findViews() {
        profilePicture = findViewById(R.id.settings_profilePicture);
        usernameLabel = findViewById(R.id.settings_username_label);
        usernameField = findViewById(R.id.settings_username_field);
        passwordLabel = findViewById(R.id.settings_password_label);
        passwordField = findViewById(R.id.settings_password_field);
        editUsernamePicture = findViewById(R.id.settings_username_edit);
        editPasswordPicture = findViewById(R.id.settings_password_edit);
        resetScoresBtn = findViewById(R.id.settings_resetScores_btn);
        deleteAccountBtn = findViewById(R.id.settings_deleteAccount_btn);

    }

    public void editViewsAttributes() {
        int profilePicWidth = getDisplayWidth()*55/100;
        int editPicWidth = getDisplayWidth()*7/100;

        // TODO: set user profile pic
        // SET PROFILE PIC SIZE
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(profilePicWidth, profilePicWidth);
        profilePicture.setLayoutParams(params);

        // SET EDIT PIC SIZE
        params = new LinearLayout.LayoutParams(editPicWidth, editPicWidth);
        editUsernamePicture.setLayoutParams(params);
        editPasswordPicture.setLayoutParams(params);

        // SET USER INFO IN TEXT
        usernameField.setText(userManager.getActiveUser().getUsername());
        passwordField.setText(userManager.getActiveUser().getPassword());
    }

    public void setClickListeners() {
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK", "PROFILE PIC");
            }
        });

        editUsernamePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldUsername = userManager.getActiveUser().getUsername();
                String newUsername = String.valueOf(usernameField.getText());
                User storedActiveUser = userManager.getUserByName(oldUsername);
                storedActiveUser.setUsername(newUsername);

                userManager.setActiveUser(storedActiveUser);
                userManager.writeUsersXML(getApplicationContext());

                Log.i(
                        "CLICK",
                        "EDIT USERNAME\n" +
                                "Old username: " + oldUsername + "\n" +
                                "New username: " + newUsername);
            }
        });

        editPasswordPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = userManager.getActiveUser().getPassword();
                String newPassword = String.valueOf(passwordField.getText());
                User storedActiveUser = userManager.getUserByName(oldPassword);
                storedActiveUser.setPassword(newPassword);

                userManager.setActiveUser(storedActiveUser);
                userManager.writeUsersXML(getApplicationContext());
                Log.i(
                        "CLICK",
                        "EDIT PASSWORD\n" +
                            "Old password: " + oldPassword + "\n" +
                            "New password: " + newPassword);
            }
        });

        resetScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldScore2048 = userManager.getActiveUser().getHighScore_2048();
                int oldScoreSenku = userManager.getActiveUser().getHighScore_Senku();

                User storedActiveUser = userManager.getUserByName(userManager.getActiveUser().getUsername());
                storedActiveUser.setHighScore_2048(0);
                storedActiveUser.setHighScore_Senku(0);

                userManager.setActiveUser(storedActiveUser);
                userManager.writeUsersXML(getApplicationContext());

                Log.i(
                        "CLICK",
                        "RESET HIGHSCORES\n" +
                                "Old 2048 Highscore: " + oldScore2048 + "\n" +
                                "Old Senku Highscore: " + oldScoreSenku + "\n" +
                                "========================================\n" +
                                "New 2048 Highscore: " + 0 + "\n" +
                                "New Senku Highscore: " + 0);
            }
        });

        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICK", "DELETE ACCOUNT");

                userManager.getUsers().remove(userManager.getUserByName(userManager.getActiveUser().getUsername()));
                userManager.setActiveUser(null);
                userManager.writeUsersXML(getApplicationContext());

                startActivity(new Intent(SettingsScreen.this, IdentificationScreen.class));

            }
        });
    }

    public int getDisplayWidth(){
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
}
