package com.example.pm_gamecenter.gameSenku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;

import com.example.pm_gamecenter.R;
import com.example.pm_gamecenter.menus.HubScreen;
import com.example.pm_gamecenter.menus.IdentificationPopup;
import com.example.pm_gamecenter.utilities.PopupActionListener;
import com.example.pm_gamecenter.utilities.User;
import com.example.pm_gamecenter.utilities.UserManager;

public class GameSenkuScreen extends AppCompatActivity implements MoveListener, PopupActionListener {
    private UserManager userManager = UserManager.getInstance();

    private Timer timer;
    private TextView currentScoreValue, bestScoreValue;
    private Board board;
    private FrameLayout timerContainer, boardContainer;
    private Button undoButton, resetButton;
    private String lastCurrentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_game_senku);

        findViews();
        editViewsAttributes();
        setClickListeners();
        restoreSavedData();
    }

    public void findViews() {
        timerContainer = findViewById(R.id.game_senku_timer_container);
        timer = new Timer(new ContextThemeWrapper(this, R.style.TimerBackgroundStyle));
        currentScoreValue = findViewById(R.id.game_senku_score_current_value);
        bestScoreValue = findViewById(R.id.game_senku_score_best_value);
        boardContainer = findViewById(R.id.game_senku_board_container);
        board = new Board(this);
        undoButton = findViewById(R.id.game_senku_button_undo);
        resetButton = findViewById(R.id.game_senku_button_reset);
    }

    public void editViewsAttributes() {
        timerContainer.addView(timer);
        timer.startTimer();

        bestScoreValue.setText(String.valueOf(userManager.getActiveUser().getHighScore_Senku()));
        lastCurrentScore = String.valueOf(currentScoreValue.getText());

        board.setMoveListener(this);
        board.initBoard();
        boardContainer.addView(board);
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

        for (Cell[] rows : board.getBoardMatrix()) {
            for (Cell c : rows) {
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            // If there is a selected cell -> Handle movement
                            if (board.getSelectedCell() != null) {
                                Board.Direction dir = board.getDirection(board.getSelectedCell(), c);
                                Log.i("SELECTED", "There is already a peg selected");

                                if (dir != null) {
                                    board.handleMovement(board.getSelectedCell(), c, dir);
                                    handleBoardStatus();
                                    Log.i("MOVEMENT", "Handling movement...");
                                }

                                Log.i("UNSELECT", "Unselecting peg...");
                                board.getSelectedCell().getCellSprite().clearColorFilter();
                                board.setSelectedCell(null);
                            }
                            // If there isn't a selected cell -> Select cell
                            else if (c.getValue() == 1){
                                Log.i("SELECTING", "Selecting peg...");
                                board.setSelectedCell(c);
                                c.getCellSprite().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dim_black));
                            }
                        } catch (Exception e) {
                            System.out.println("[ERROR] " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void restoreSavedData() {
        SharedPreferences prefs = getSharedPreferences("PREFS_SENKU", MODE_PRIVATE);

        timer.setSeconds(prefs.getInt("TIMER_SECONDS", 0));
        timer.setMinutes(prefs.getInt("TIMER_MINUTES", 0));

        int currentScore = prefs.getInt("CURRENT_SCORE", 0);
        currentScoreValue.setText(String.valueOf(currentScore));

        String serializedBoard = prefs.getString("BOARD_VALUES", "");
        deserializeBoardValues(serializedBoard);
    }

    private void undoGame() {
        board.undoBoard();
        currentScoreValue.setText(lastCurrentScore);
    }

    private void resetGame() {
        board.resetBoard();
        currentScoreValue.setText("0");
    }

    public void handleBoardStatus() {
        switch (board.getBoardStatus()) {
            case WIN: {
                Log.i("STATUS", "Win");

                showPopup(GameEndPopup.GameOver.WIN);
            }
            case LOST: {
                Log.i("STATUS", "Lost");

                showPopup(GameEndPopup.GameOver.LOST);
            }
            case PLAYABLE: {
                Log.i("STATUS", "Playable");

            }
        }
    }

    public void showPopup(GameEndPopup.GameOver gameOverType) {
        int popupWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int popupHeight = ViewGroup.LayoutParams.MATCH_PARENT;

        GameEndPopup gameOverPopup = new GameEndPopup(this, gameOverType, popupWidth, popupHeight);
        gameOverPopup.setPopupActionListener(this);
        gameOverPopup.showAtLocation(board, Gravity.CENTER, 0, 0);
    }

    public String serializeBoardValues() {
        StringBuilder serializedBuilder = new StringBuilder();

        for (int row = 0; row < board.getBoardMatrix().length; row++) {
            for (int col = 0; col < board.getBoardMatrix()[row].length; col++) {
                // Append raw values
                serializedBuilder.append(board.getBoardMatrix()[row][col].getValue());
                if (col < board.getBoardMatrix()[row].length -1 ) { // Ensure that a separator isn't appended in the last column of the row
                    serializedBuilder.append(","); // Delimiting items in a row.
                }
            }
            if (row < board.getBoardMatrix().length -1 ) {
                serializedBuilder.append(";"); // End of row.
            }
        }
        return serializedBuilder.toString();
    }

    public void deserializeBoardValues(String serializedBoard) {
        String[] serializedRows = serializedBoard.split(";");

        for (int row = 0; row < serializedRows.length; row++) {
            String[] serializedCols = serializedRows[row].split(",");
            for (int col = 0; col < serializedCols.length; col++) {
                board.getBoardMatrix()[row][col].updateCell(Integer.parseInt(serializedCols[col]));
            }
        }
    }

    @Override
    public void onMoveMade() {
        lastCurrentScore = String.valueOf(currentScoreValue.getText());
        User storedActiveUser = userManager.getUserByName(userManager.getActiveUser().getUsername());
        int currentScoreIntValue = Integer.parseInt(String.valueOf(currentScoreValue.getText())) + 1;

        currentScoreValue.setText(String.valueOf(currentScoreIntValue));

        if (storedActiveUser.getHighScore_Senku() < currentScoreIntValue) {
            storedActiveUser.setHighScore_Senku(currentScoreIntValue);
            bestScoreValue.setText(String.valueOf(currentScoreIntValue));
            userManager.setActiveUser(storedActiveUser);
            userManager.writeUsersXML(this);
        };
    }

    @Override
    public void onPopupDismissed() {
        startActivity(new Intent(GameSenkuScreen.this, HubScreen.class));
    }

    @Override
    protected void onPause() {  // Method to save data after leaving from the activity
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("PREFS_SENKU", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("TIMER_SECONDS", timer.getSeconds());
        editor.putInt("TIMER_MINUTES", timer.getMinutes());
        editor.putString("BOARD_VALUES", serializeBoardValues());
        editor.putInt("CURRENT_SCORE", Integer.parseInt(currentScoreValue.getText().toString()));
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("TIMER_SECONDS", timer.getSeconds());
        outState.putInt("TIMER_MINUTES", timer.getMinutes());
        outState.putString("CURRENT_SCORE", currentScoreValue.getText().toString());
        outState.putString("BOARD_VALUES", serializeBoardValues());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentScoreValue.setText(savedInstanceState.getString("CURRENT_SCORE", "0"));
        deserializeBoardValues(savedInstanceState.getString("BOARD_VALUES"));
    }

}
