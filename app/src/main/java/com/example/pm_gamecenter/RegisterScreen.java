package com.example.pm_gamecenter;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterScreen extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identification_form);

        userManager = UserManager.getInstance();
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

        if (userExists(name)) {
            // TODO: add poopuser already exists, enter another username
        } else {

            // TODO: add popup (success, back to main menu)
        }


    }

    public boolean userExists (String name) {
        for (User u : userManager.getUsers()) {
            if (name.equals(u.getUsername())) {
                return true;
            }
        }

        return false;
    }
}
