package com.example.pm_gamecenter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm_gamecenter.R;

public class LoginScreen extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private User userLogged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        nameEditText = findViewById(R.id.inputText_name);
        passwordEditText = findViewById(R.id.inputText_password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            loginUser();
        });
    }

    private void loginUser() {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // TODO: Read info from users.xml and load profile.
    }
}
