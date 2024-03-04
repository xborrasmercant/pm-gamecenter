package com.example.pm_gamecenter.gameSenku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.pm_gamecenter.R;


import androidx.core.content.ContextCompat;

public class Cell extends FrameLayout {
    private int colPos, rowPos, value;
    private ImageView cellSprite;


    public Cell (Context context, int colPos, int rowPos, int value) {
        super(context);

        this.colPos = colPos;
        this.rowPos = rowPos;
        this.cellSprite = new ImageView(context);
        updateCell(value);
        addComponentsToLayout();
    }

    // METHODS

    public void addComponentsToLayout() {
        int cellMargin = (int) (getDisplayWidth()*1.2/100); // Cell margin amount equals to the 3% of the displayWidth
        int cellSize = (int) ((getDisplayWidth() - cellMargin * 14) / 7.7); // Cell size equals to the displayWidth minus Cell Margin * 14 (because of left and right margins) divided by the amount of pegs (7)

        FrameLayout.LayoutParams params = new LayoutParams(cellSize, cellSize);
        params.setMargins(cellMargin, cellMargin, cellMargin, cellMargin);
        cellSprite.setLayoutParams(params);
        addView(cellSprite);
    }

    public void updateCell(int value) {
        setValue(value);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_beige));

        if (value == 1) {
            cellSprite.setImageResource(R.drawable.sprite_cellpeg);
        } else if (value == 0) {
            cellSprite.setImageResource(R.drawable.sprite_cellnull);
            cellSprite.clearColorFilter();

        } else {
            cellSprite.setVisibility(View.INVISIBLE);
            this.setVisibility(View.INVISIBLE);
        }
    }

    public int getDisplayWidth(){
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    // GETTERS and SETTERS
    public int getColPos() {
        return colPos;
    }
    public void setColPos(int colPos) {
        this.colPos = colPos;
    }
    public int getRowPos() {
        return rowPos;
    }
    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public ImageView getCellSprite() {
        return cellSprite;
    }

}
