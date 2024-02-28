package com.example.pm_gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IdentificationScreen extends AppCompatActivity {
    private Button loginButton, registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identificaton_screen);

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentificationScreen.this, IdentificationForm.class);
                intent.putExtra("IdentificationType", IdentificationType.LOGIN);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentificationScreen.this, IdentificationForm.class);
                intent.putExtra("IdentificationType", IdentificationType.REGISTER);
                startActivity(intent);            }
        });
    }


}
