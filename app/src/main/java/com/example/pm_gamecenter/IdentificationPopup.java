package com.example.pm_gamecenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class IdentificationPopup extends PopupWindow{
    private TextView title;
    private TextView description;
    private Button continueButton;
    private View popupView;
    public enum Popup {
        LOGIN,
        REGISTER
    }

    public IdentificationPopup(Context context, Popup popup) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_identification, null);
        this.setContentView(popupView);
        this.setFocusable(true);
        findViews();
        setPopupInfo(popup);
    }

    public void findViews() {
        title = popupView.findViewById(R.id.popup_title);
        description = popupView.findViewById(R.id.popup_description);
        continueButton = popupView.findViewById(R.id.popup_button);

        // Set listener
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {dismiss();}
        });
    }

    public void setPopupInfo(Popup popup) {
        switch (popup) {
            case LOGIN: {
                title.setText(R.string.popup_loginFailed_Title);
                description.setText(R.string.popup_loginFailed_Description);
            }
            case REGISTER: {
                title.setText(R.string.popup_registerFailed_Title);
                description.setText(R.string.popup_registerFailed_Description);
            }
        }
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public Button getContinueButton() {
        return continueButton;
    }

    public void setContinueButton(Button continueButton) {
        this.continueButton = continueButton;
    }

}