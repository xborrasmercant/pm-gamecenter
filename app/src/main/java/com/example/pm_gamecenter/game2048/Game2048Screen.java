package com.example.pm_gamecenter.game2048;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.pm_gamecenter.R;
import com.example.pm_gamecenter.User;
import com.example.pm_gamecenter.UserManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class Game2048Screen extends AppCompatActivity implements MergeListener {
    private UserManager userManager = UserManager.getInstance();
    private TextView currentScoreValue, bestScoreValue;
    private ImageView gameLogo;
    private Grid grid;
    private FrameLayout layoutBody;
    private Button undoButton, resetButton;
    private GestureDetector gestureDetector;
    private String lastCurrentScore;
    private static final int MIN_SWIPE_DISTANCE = 120;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_game2048);


        findViews();
        editViewsAttributes();
        setClickListeners();
        setGestureDetector();
        restoreSavedData();

    }

    // METHODS

    public void restoreSavedData() {
        SharedPreferences prefs = getSharedPreferences("PREFS_2048", MODE_PRIVATE);
        int currentScore = prefs.getInt("CURRENT_SCORE", 0);
        currentScoreValue.setText(String.valueOf(currentScore));
        String serializedGrid = prefs.getString("GRID_VALUES", "");
        deserializeGridValues(serializedGrid);
    }

    public void findViews() {
        gameLogo = findViewById(R.id.game2048_logo);
        currentScoreValue = findViewById(R.id.game2048_score_current_value);
        bestScoreValue = findViewById(R.id.game2048_score_best_value);
        layoutBody = findViewById(R.id.game2048_body);
        grid = new Grid(this, 4, 4);
        grid.setId(View.generateViewId());
        grid.setMergeListener(this);
        layoutBody.addView(grid);
        undoButton = findViewById(R.id.game2048_button_undo);
        resetButton = findViewById(R.id.game2048_button_reset);
    }

    public void editViewsAttributes() {
        int gameLogoSize = getDisplayWidth()*35/100;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gameLogoSize, gameLogoSize);
        params.setMargins(16,0, 16,0);
        gameLogo.setLayoutParams(params);

        Log.i("ACTIVE_USER_2048_HIGHSCORE", String.valueOf(userManager.getActiveUser().getHighScore_2048()));
        bestScoreValue.setText(String.valueOf(userManager.getActiveUser().getHighScore_2048()));

        lastCurrentScore = String.valueOf(currentScoreValue.getText());

        grid.setBackground(getDrawableBackground(ContextCompat.getColor(this.getBaseContext(), R.color.mid_olive_UI)));
    }

    public void setClickListeners() {
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoGame();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    public void setGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                String direction = "NULL";

                lastCurrentScore = String.valueOf(currentScoreValue.getText());

                float deltaX = e2.getX() - e1.getX();
                float deltaY = e2.getY() - e1.getY();
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    // Horizontal swipe
                    if (Math.abs(deltaX) > MIN_SWIPE_DISTANCE) {
                        if (deltaX > 0) {
                            direction = "RIGHT";

                        } else {
                            direction = "LEFT";

                        }
                    }
                } else {
                    // Vertical swipe
                    if (Math.abs(deltaY) > MIN_SWIPE_DISTANCE) {
                        if (deltaY > 0) {
                            direction = "DOWN";
                        } else {
                            direction = "UP";
                        }
                    }
                }
                Log.d("Gesture", "Fling detected: " + direction);
                updateGameActivity(direction);
                return true;
            }
        });
    }

    private void updateGameActivity(String direction) {
        grid.handleSweep(direction);
        grid.valueToRandomGameBlock();
        resizeGameBlockText();
    }

    public void resizeGameBlockText() {
        for (Block[] row : grid.getGameBlockMatrix()) {
            for (Block gb : row) {
                gb.getTextView().setTextSize(getResponsiveTextSize(gb.getValue()));
            }
        }
    }
    private void undoGame() {
        grid.undoGrid();
        currentScoreValue.setText(lastCurrentScore);
    }

    private void resetGame() {
        grid.resetGrid();
        currentScoreValue.setText("0");
    }

    private GradientDrawable getDrawableBackground(int BGColor) {
        GradientDrawable newBackground = new GradientDrawable();
        newBackground.setShape(GradientDrawable.RECTANGLE);
        newBackground.setColor(BGColor);
        newBackground.setCornerRadius(15);

        return newBackground;
    }

    public int getResponsiveTextSize(int val) {
        int baseSize = (int) (getDisplayWidth()*0.05);
        float scaleFactor = 0.83f;

        int valDigits = String.valueOf(val).length();

        return (int) (baseSize * Math.pow(scaleFactor, valDigits - 1));
    }

    public int getDisplayWidth(){
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    @Override
    public void onMerged(int mergedValue) {
        User storedActiveUser = userManager.getUserByName(userManager.getActiveUser().getUsername());
        int currentScoreIntValue = Integer.parseInt(String.valueOf(currentScoreValue.getText()))  + mergedValue;

        currentScoreValue.setText(String.valueOf(currentScoreIntValue));

        if (storedActiveUser.getHighScore_2048() < currentScoreIntValue) {
            storedActiveUser.setHighScore_2048(currentScoreIntValue);
            bestScoreValue.setText(String.valueOf(currentScoreIntValue));
            userManager.setActiveUser(storedActiveUser);
            userManager.writeUsersXML(this);
        }
    }

    public String serializeGridValues() {
        StringBuilder serializedBuilder = new StringBuilder();

        for (int row = 0; row < grid.getGameBlockMatrix().length; row++) {
            for (int col = 0; col < grid.getGameBlockMatrix()[row].length; col++) {
                // Append raw values
                serializedBuilder.append(grid.getGameBlockMatrix()[row][col].getValue());
                if (col < grid.getGameBlockMatrix()[row].length -1 ) { // Ensure that a separator isn't appended in the last column of the row
                    serializedBuilder.append(","); // Delimiting items in a row.
                }
            }
            if (row < grid.getGameBlockMatrix().length -1 ) {
                serializedBuilder.append(";"); // End of row.
            }
        }
        return serializedBuilder.toString();
    }

    public void deserializeGridValues(String serializedGrid) {
        String[] serializedRows = serializedGrid.split(";");

        for (int row = 0; row < serializedRows.length; row++) {
            String[] serializedCols = serializedRows[row].split(",");
            for (int col = 0; col < serializedCols.length; col++) {
                grid.getGameBlockMatrix()[row][col].setValue(Integer.parseInt(serializedCols[col]));
            }
        }
    }

    @Override
    protected void onPause() {  // Method to save data after leaving from the activity
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("PREFS_2048", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("GRID_VALUES", serializeGridValues());
        editor.putInt("CURRENT_SCORE", Integer.parseInt(currentScoreValue.getText().toString()));
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("CURRENT_SCORE", currentScoreValue.getText().toString());
        outState.putString("GRID_VALUES", serializeGridValues());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentScoreValue.setText(savedInstanceState.getString("CURRENT_SCORE", "0"));
        deserializeGridValues(savedInstanceState.getString("GRID_VALUES"));
    }

}