package com.example.pm_gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm_gamecenter.R;

public class LoginScreen extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private UserManager userManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        userManager = UserManager.getInstance();
        nameEditText = findViewById(R.id.inputText_name);
        passwordEditText = findViewById(R.id.inputText_password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            IdentificationPopup idPopup = new IdentificationPopup(this, IdentificationPopup.Popup.LOGIN);
            idPopup.showAtLocation(loginButton, Gravity.CENTER, 0, 0);
        });
    }

    private void loginUser() {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // If login success set active user and go to hub (where games are located)
        if (userExists(name, password)) {
            userManager.setActiveUser(new User(name, password));
            startActivity(new Intent(LoginScreen.this, HubScreen.class));
        } else {
            // TODO: add popup (user or password incorrect, try again)
        }
    }

    public boolean userExists (String name, String password) {
        for (User u : userManager.getUsers()) {
            if (name.equals(u.getUsername()) || password.equals(u.getPassword())) {
                return true;
            }
        }

        return false;
    }
}
