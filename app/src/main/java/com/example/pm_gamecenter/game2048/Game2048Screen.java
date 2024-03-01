package com.example.pm_gamecenter.game2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.pm_gamecenter.R;
import com.example.pm_gamecenter.User;
import com.example.pm_gamecenter.UserManager;


public class Game2048Screen extends AppCompatActivity implements MergeListener {
    private UserManager userManager = UserManager.getInstance();
    ScoreBox currentScore, bestScore;
    TextView gameLogo;
    Grid grid;
    FrameLayout playableArea;
    GestureDetector gestureDetector;
    ConstraintLayout gameLayout;
    ConstraintLayout.LayoutParams mainCLayoutParams;
    LinearLayout footer;
    Button undoBtn, resetBtn;
    Typeface mainTypeface;



    private static final int MIN_SWIPE_DISTANCE = 120; // Adjust this based on your needs

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Ensure that the GestureDetector gets the touch events
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameLayout = new ConstraintLayout(getBaseContext());
        setContentView(gameLayout);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                String direction = "NULL";

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
        
        addComponentsToLayout();
        configureConstraints();
        stylizeComponents();
    }


    // METHODS
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


    private void stylizeComponents() {
        //gameLayout.setBackgroundResource(R.drawable.dimblack_background);
        gameLayout.setBackground(getDrawableBackground(ContextCompat.getColor(this, R.color.black)));

        gameLogo.setTextSize(getResponsiveTextSize(2048)*1.8f);
        gameLogo.setText("2048");
        gameLogo.setTextColor(ContextCompat.getColor(this.getBaseContext(), R.color.white));
        gameLogo.setGravity(Gravity.CENTER);
        gameLogo.setShadowLayer(5, 6, 6, ContextCompat.getColor(this.getBaseContext(), R.color.shadowColor));
        gameLogo.setTypeface(ResourcesCompat.getFont(getBaseContext(), R.font.roboto_black));
        gameLogo.setWidth((int) (getDisplayWidth()/3*1.15));
        gameLogo.setHeight((int) (getDisplayWidth()/3*1.15));
        gameLogo.setBackground(getDrawableBackground(ContextCompat.getColor(this.getBaseContext(), R.color.tier4_2)));

        grid.setBackground(getDrawableBackground(ContextCompat.getColor(this.getBaseContext(), R.color.mid_olive_UI)));

        int footerTextSize = 32;

        footer.setGravity(Gravity.CENTER);
        footer.setOrientation(LinearLayout.HORIZONTAL);

        undoBtn.setText("UNDO");
        undoBtn.setTypeface(mainTypeface);
        undoBtn.setTextSize(footerTextSize);
        undoBtn.setTextColor(ContextCompat.getColor(this.getBaseContext(), R.color.white));
        undoBtn.setBackgroundResource(R.drawable.default_button);
        //undoBtn.setBackground(getDrawableBackground(ContextCompat.getColor(this.getBaseContext(), R.color.mid_olive_UI)));

        resetBtn.setText("RESET");
        resetBtn.setTypeface(mainTypeface);
        resetBtn.setTextSize(footerTextSize);
        resetBtn.setTextColor(ContextCompat.getColor(this.getBaseContext(), R.color.white));
        resetBtn.setBackgroundResource(R.drawable.default_button);
        //resetBtn.setBackground(getDrawableBackground(ContextCompat.getColor(this.getBaseContext(), R.color.mid_olive_UI)));
    }

    private void addComponentsToLayout() {
        int spacing = 24;
        mainTypeface = ResourcesCompat.getFont(this, R.font.roboto_black);

        gameLogo = new TextView(getBaseContext());
        gameLogo.setId(View.generateViewId());
        gameLayout.addView(gameLogo);

        bestScore = new ScoreBox(getBaseContext(), userManager.getActiveUser().getHighScore_2048(), "Best");
        bestScore.setId(View.generateViewId());
        gameLayout.addView(bestScore);

        currentScore = new ScoreBox(getBaseContext(), 0, "Score");
        currentScore.setId(View.generateViewId());
        gameLayout.addView(currentScore);


        grid = new Grid(getBaseContext(), 4, 4);
        grid.setId(View.generateViewId());
        grid.setMergeListener(this);
        gameLayout.addView(grid);

        footer = new LinearLayout(getBaseContext());
        footer.setId(View.generateViewId());
        gameLayout.addView(footer);
        extendViewWidth(footer);

        undoBtn = new Button(getBaseContext());
        undoBtn.setId(View.generateViewId());
        setFooterParams(undoBtn, 0, 16);
        footer.addView(undoBtn);

        resetBtn = new Button(getBaseContext());
        resetBtn.setId(View.generateViewId());
        resetBtn.setOnClickListener(v -> resetGame());
        setFooterParams(resetBtn, 16, 0);
        footer.addView(resetBtn);
    }

    private void configureConstraints() {
        // ConstraintSet Configuration
        ConstraintSet cs = new ConstraintSet();
        int spacing = 32;
        int currentScoreID = currentScore.getId();
        int bestScoreID = bestScore.getId();
        int gameGridID = grid.getId();
        int parentID = ConstraintSet.PARENT_ID;
        int gameLogoID = gameLogo.getId();
        int resetBtnID = resetBtn.getId();
        int undoBtnID = undoBtn.getId();
        int footerID = footer.getId();

        cs.clone(gameLayout); // Clone the constraints of gameLayout into ConstraintSet

        cs.setVerticalBias(gameLogoID, 0.5f);
        cs.setVerticalBias(bestScoreID, 0.9f);


        cs.connect(gameLogoID, ConstraintSet.TOP, parentID, ConstraintSet.TOP, spacing);
        cs.connect(gameLogoID, ConstraintSet.START, parentID, ConstraintSet.START, spacing);
        cs.connect(gameLogoID, ConstraintSet.BOTTOM, gameGridID, ConstraintSet.TOP, spacing);

        // CurrentScore
        cs.connect(currentScoreID, ConstraintSet.END, parentID, ConstraintSet.END, spacing);
        cs.connect(currentScoreID, ConstraintSet.TOP, parentID, ConstraintSet.TOP, spacing);
        cs.connect(currentScoreID, ConstraintSet.START, gameLogoID, ConstraintSet.END, spacing);

        // BestScore
        cs.connect(bestScoreID, ConstraintSet.END, parentID, ConstraintSet.END, spacing);
        cs.connect(bestScoreID, ConstraintSet.START, gameLogoID, ConstraintSet.END, spacing);
        cs.connect(bestScoreID, ConstraintSet.BOTTOM, gameGridID, ConstraintSet.TOP, spacing);

        // CurrentScore <---> BestScore
        cs.connect(currentScoreID, ConstraintSet.BOTTOM, bestScoreID, ConstraintSet.TOP, spacing/2);
        cs.connect(bestScoreID, ConstraintSet.TOP, currentScoreID, ConstraintSet.BOTTOM, spacing/2);

        cs.connect(gameGridID, ConstraintSet.START, parentID, ConstraintSet.START, spacing);
        cs.connect(gameGridID, ConstraintSet.END, parentID, ConstraintSet.END, spacing);
        cs.connect(gameGridID, ConstraintSet.BOTTOM, footerID, ConstraintSet.TOP, spacing);

        cs.connect(footerID, ConstraintSet.START, parentID, ConstraintSet.START, spacing);
        cs.connect(footerID, ConstraintSet.END, parentID, ConstraintSet.END, spacing);
        cs.connect(footerID, ConstraintSet.BOTTOM, gameGridID, ConstraintSet.TOP, spacing);
        cs.connect(footerID, ConstraintSet.BOTTOM, parentID, ConstraintSet.BOTTOM, spacing);

        cs.applyTo(gameLayout);
    }

    private void resetGame() {
        grid.resetGrid();
        currentScore.setScoreValue(0);
    }

    private GradientDrawable getDrawableBackground(int BGColor) {
        GradientDrawable newBackground = new GradientDrawable();
        newBackground.setShape(GradientDrawable.RECTANGLE);
        newBackground.setColor(BGColor);
        newBackground.setCornerRadius(15);

        return newBackground;
    }

    private void extendViewWidth(View view) {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
    }

    private void setFooterParams(View view, int lMargin, int rMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(lMargin,0, rMargin,0);

        view.setLayoutParams(params);
    }

    public int getResponsiveTextSize(int val) {
        int baseSize = (int) (getDisplayWidth()*0.05);
        float scaleFactor = 0.83f;

        int valDigits = String.valueOf(val).length();

        return (int) (baseSize * Math.pow(scaleFactor, valDigits - 1));
    }

    public GridLayout.LayoutParams createGridLayoutParams(int x, int y, int spacing) {

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(x);
        params.columnSpec = GridLayout.spec(y);
        params.setMargins(spacing,spacing,spacing,spacing);
        grid.setPadding(spacing, spacing, spacing, spacing);

        return params;
    }
    public int getDisplayWidth(){
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    @Override
    public void onMerged(int mergedValue) {
        User storedActiveUser = userManager.getUserByName(userManager.getActiveUser().getUsername());
        int currentScoreValue = currentScore.getScoreValue() + mergedValue;

        currentScore.setScoreValue(currentScoreValue);

        if (storedActiveUser.getHighScore_2048() < currentScoreValue) {
            storedActiveUser.setHighScore_2048(currentScoreValue);
            bestScore.setScoreValue(currentScoreValue);
            userManager.setActiveUser(storedActiveUser);
            userManager.writeUsersXML(this);
        }

    }

}