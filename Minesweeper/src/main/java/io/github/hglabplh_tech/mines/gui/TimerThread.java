package io.github.hglabplh_tech.mines.gui;

import javax.swing.*;

public class TimerThread implements Runnable {
    private static long seconds = 115;
    private final JLabel timerLabel;

    public TimerThread(StatusPanel panel) {
        this.timerLabel = panel.getTimerLabel();
    }
    public void run() {
        while (true) {
            GUILogics.waitSeconds(2);
            TimerThread.seconds--;
            timerLabel.setText(String.valueOf(getTheSeconds()));
        }
    }

    public static long getTheSeconds() {
        return TimerThread.seconds;
    }
}
