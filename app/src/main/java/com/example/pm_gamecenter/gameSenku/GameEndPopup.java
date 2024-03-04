package com.example.pm_gamecenter.gameSenku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.pm_gamecenter.utilities.PopupActionListener;
import com.example.pm_gamecenter.R;

public class GameEndPopup extends PopupWindow{
    private TextView title;
    private TextView description;
    private Button continueButton;
    private View popupView;
    private PopupActionListener popupActionListener;
    public enum GameOver {
        WIN, LOST
    }


    public GameEndPopup(Context context, GameOver gameOverType, int width, int height) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_gameover, null);
        this.setContentView(popupView);

        findViews();
        setPopupInfo(gameOverType, width, height);
        setOnClickListener(gameOverType);
    }

    public void findViews() {
        title = popupView.findViewById(R.id.popup_gameover_title);
        description = popupView.findViewById(R.id.popup_gameover_description);
        continueButton = popupView.findViewById(R.id.popup_gameover_button);
    }

    public void setPopupInfo(GameOver gameOverType, int width, int height) {
        setWidth(width);
        setHeight(height);

        switch (gameOverType) {
            case WIN: {
                title.setText(R.string.popup_gameWin_Title);
                description.setText(R.string.popup_gameWin_Description);
                break;
            }
            case LOST: {
                title.setText(R.string.popup_gameLost_Title);
                description.setText(R.string.popup_gameLost_Description);
                break;
            }
        }
    }

    public void setOnClickListener(GameOver gameOverType) {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (popupActionListener != null) {
                        popupActionListener.onPopupDismissed();
                    }
                    dismiss();
            }
        });
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

    public void setPopupActionListener(PopupActionListener popupActionListener) {
        this.popupActionListener = popupActionListener;
    }

}