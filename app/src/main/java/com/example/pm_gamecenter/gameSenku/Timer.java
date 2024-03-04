package com.example.pm_gamecenter.gameSenku;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public class Timer extends androidx.appcompat.widget.AppCompatTextView {
    private boolean runTimer = true;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int m = 0, s = 0;

    public Timer(Context context) {
        super(context);

        setText(String.format("%02d:%02d", 0, 0));
        setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void startTimer() {
        runTimer = true;
        updateTimer();
    }

    public void stopTimer() {
        runTimer = false;
    }

    private void updateTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Increment seconds
                while (runTimer){
                    s++;
                    // When seconds reach 60, increment minutes and reset seconds
                    if (s == 60) {
                        m++;
                        s = 0;
                    }
                    // Reset timer after 59 minutes
                    if (m == 60) {
                        m = 0;
                    }

                    Timer.this.setText(String.format("%02d:%02d", m, s));
                }
            }
        }, 1000); // Schedule the update after 1 second
    }

    public boolean isRunTimer() {
        return runTimer;
    }

    public void setRunTimer(boolean runTimer) {
        this.runTimer = runTimer;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public int getMinutes() {
        return m;
    }

    public void setMinutes(int m) {
        this.m = m;
    }

    public int getSeconds() {
        return s;
    }

    public void setSeconds(int s) {
        this.s = s;
    }
}
