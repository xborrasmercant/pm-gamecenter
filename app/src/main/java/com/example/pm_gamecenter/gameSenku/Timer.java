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
        handler.removeCallbacksAndMessages(null); // This will remove any pending posts of the Runnable
    }

    private void updateTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!runTimer) {
                    return; // Exit if the timer is stopped
                }
                s++;
                if (s == 60) {
                    m++;
                    s = 0;
                }
                if (m == 60) {
                    m = 0;
                }
                Timer.this.setText(String.format("%02d:%02d", m, s));
                // Post the Runnable to itself to run again after 1000ms
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public void resetTimer() {
        m = 0;
        s = 0;
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
