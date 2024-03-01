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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class IdentificationScreen extends AppCompatActivity {

    private UserManager userManager = UserManager.getInstance();
    private ImageView appLogo;
    private Button loginButton, registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_identificaton);

        findViews();
        editViewsAttributes();
        setClickListeners();
    }


    public void findViews() {
        appLogo = findViewById(R.id.app_logo);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
    }

    public void editViewsAttributes() {
        int appLogoWidth = getDisplayWidth()*60/100;
        int appLogoTopMargin = getDisplayHeight()*10/100;

        userManager = UserManager.getInstance();
        //userManager.resetUsersXML(this);
        userManager.parseUsersXML(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(appLogoWidth, appLogoWidth);
        params.setMargins(0, appLogoTopMargin, 0, 0);
        appLogo.setLayoutParams(params);



    }

    public void setClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentificationScreen.this, IdentificationFormScreen.class);
                intent.putExtra("IdentificationType", IdentificationType.LOGIN);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentificationScreen.this, IdentificationFormScreen.class);
                intent.putExtra("IdentificationType", IdentificationType.REGISTER);
                startActivity(intent);
            }
        });
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
