package com.example.pm_gamecenter;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm_gamecenter.R;

public class RegisterScreen extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        nameEditText = findViewById(R.id.inputText_name);
        passwordEditText = findViewById(R.id.inputText_password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(view -> {
            registerUser();
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // TODO: Write user information to users.xml
    }
}
