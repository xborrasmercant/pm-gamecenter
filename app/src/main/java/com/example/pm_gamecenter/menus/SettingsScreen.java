package com.example.pm_gamecenter.menus;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pm_gamecenter.R;
import com.example.pm_gamecenter.utilities.User;
import com.example.pm_gamecenter.utilities.UserManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class SettingsScreen extends AppCompatActivity {

    private UserManager userManager = UserManager.getInstance();
    private ActivityResultLauncher<Intent> resultLauncher;
    private ImageView profilePicture, editUsernamePicture, editPasswordPicture;
    private TextView usernameLabel, usernameField, passwordLabel, passwordField;
    private Button resetScoresBtn, deleteAccountBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_settings);
        registerResult();

        findViews();
        editViewsAttributes();
        setClickListeners();
        onBackResume();
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

        // SET PROFILE PIC SIZE
        setProfilePicture(profilePicture, userManager.getActiveUser().getUsername());
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
                pickProfilePicture();
            }
        });

        editUsernamePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldUsername = userManager.getActiveUser().getUsername();
                String newUsername = String.valueOf(usernameField.getText());
                User storedActiveUser = userManager.getUserByName(oldUsername);
                storedActiveUser.setUsername(newUsername);
                renameProfilePicture(oldUsername, newUsername);
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

                deleteProfilePicture(userManager.getActiveUser().getUsername());

                userManager.getUsers().remove(userManager.getUserByName(userManager.getActiveUser().getUsername()));
                userManager.setActiveUser(null);
                userManager.writeUsersXML(getApplicationContext());

                startActivity(new Intent(SettingsScreen.this, IdentificationScreen.class));
                finish();
            }
        });
    }

    public void registerResult() {
        // Initialize the ActivityResultLauncher
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            saveProfilePicture(imageUri, userManager.getActiveUser().getUsername());
                            setProfilePicture(profilePicture, userManager.getActiveUser().getUsername());

                        } else {
                            Toast.makeText(SettingsScreen.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void pickProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
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

    private void saveProfilePicture(Uri imageUri, String username) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            // Create a directory for your user profile pictures
            File pictureDir = new File(getFilesDir(), "ProfilePictures");
            if (!pictureDir.exists()) {
                pictureDir.mkdir();
            }
            File profilePictureFile = new File(pictureDir, username + ".png");
            try (FileOutputStream out = new FileOutputStream(profilePictureFile)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProfilePicture(String username) {
        File pictureDir = new File(getFilesDir(), "ProfilePictures");
        File profilePicture = new File(pictureDir, username + ".png");
        if (profilePicture.exists()) {
            if (profilePicture.delete()) {
                Log.i("DELETE", "Profile picture deleted successfully.");
            } else {
                Log.e("DELETE", "Failed to delete profile picture.");
            }
        }
    }

    private void renameProfilePicture(String oldUsername, String newUsername) {
        File pictureDir = new File(getFilesDir(), "ProfilePictures");
        File profilePicture = new File(pictureDir, oldUsername + ".png");
        if (profilePicture.exists()) {
            if (profilePicture.renameTo(new File(pictureDir, newUsername + ".png"))) {
                Log.i("DELETE", "Profile picture deleted successfully.");
            } else {
                Log.e("DELETE", "Failed to delete profile picture.");
            }
        }
    }

    public int getDisplayWidth(){
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public void onBackResume() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(SettingsScreen.this, HubScreen.class);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
