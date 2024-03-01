package com.example.pm_gamecenter.game2048;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.pm_gamecenter.R;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class Block extends FrameLayout {

    private int displayWidth, displayHeight;
    private int posX, posY, value;
    private final TextView textView;

    public Block(Context context, int posX, int posY, int value) {
        super(context);
        setDisplaySizes();

        this.posX = posX;
        this.posY = posY;
        this.textView = new TextView(context);
        this.setValue(value);
        this.textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.roboto_medium));
        addComponentsToLayout();
    }

    // METHODS
    public void addComponentsToLayout() {
        textView.setLayoutParams(createFrameLayoutParams());
        textView.setGravity(Gravity.CENTER);
        addView(textView);
    }

    public void setBlockTier(int value) {
        int backgroundColorResId, textColorResID;

        if (value>4096) {
            textColorResID = R.color.light_text;
            backgroundColorResId = R.color.tier7;
        }
        else {
            switch (value) {
                case 2:
                    textColorResID = R.color.dark_text;
                    backgroundColorResId = R.color.tier1_1;
                    break;
                case 4:
                    textColorResID = R.color.dark_text;
                    backgroundColorResId = R.color.tier1_2;
                    break;
                case 8:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier2_1;
                    break;
                case 16:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier2_2;
                    break;
                case 32:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier3_1;
                    break;
                case 64:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier3_2;
                    break;
                case 128:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier4_1;
                    break;
                case 256:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier4_2;
                    break;
                case 512:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier5_1;
                    break;
                case 1024:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier5_2;
                    break;
                case 2048:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier6_1;
                    break;
                case 4096:
                    textColorResID = R.color.light_text;
                    backgroundColorResId = R.color.tier6_2;
                    break;
                default:
                    textColorResID = R.color.dark_text;
                    backgroundColorResId = R.color.tier0;
                    break;
            }
        }

        int TextColor = ContextCompat.getColor(this.getContext(), textColorResID);
        int BGColor = ContextCompat.getColor(this.getContext(), backgroundColorResId);


        this.setBackground(getDrawableBackground(BGColor));
        textView.setBackground(getDrawableBackground(BGColor));
        textView.setTextColor(TextColor);
    }

    private GradientDrawable getDrawableBackground(int BGColor) {
        GradientDrawable newBackground = new GradientDrawable();
        newBackground.setShape(GradientDrawable.RECTANGLE);
        newBackground.setColor(BGColor);
        newBackground.setCornerRadius(15);

        return newBackground;
    }

    public FrameLayout.LayoutParams createFrameLayoutParams() {
        int cellMargin = (int) (displayWidth*1.2/100);
        int cellSize = (int) ((displayWidth - cellMargin * 16) / 4.4);

        FrameLayout.LayoutParams params = new LayoutParams(cellSize, cellSize);
        params.setMargins(cellMargin, cellMargin, cellMargin, cellMargin);
        return params;
    }

    public void setDisplaySizes() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;
    }

    // GETTERS AND SETTERS
    public void setValue (int value) {
        this.value = value;

        if (value!=0) {
            textView.setText(String.valueOf(value));
        }
        else {
            textView.setText("");
        }

        this.setBlockTier(value);
    }

    public int getValue() {
        return value;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public TextView getTextView() {
        return textView;
    }
}
