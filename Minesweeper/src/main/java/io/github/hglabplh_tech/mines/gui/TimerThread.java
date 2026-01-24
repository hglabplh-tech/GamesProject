/*
Copyright (c) 2025 Harald Glab-Plhak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package io.github.hglabplh_tech.mines.gui;

import io.github.hglabplh_tech.games.backend.util.ResourceHandler;

import javax.swing.*;

public class TimerThread implements Runnable {
    private static long seconds = resetTimer();
    private final JLabel timerLabel;

    public TimerThread(StatusPanel panel) {
        this.timerLabel = panel.getTimerLabel();
    }
    public void run() {
        while (true) {
            ResourceHandler.waitSeconds(1.0f);
            TimerThread.seconds--;
            timerLabel.setText(String.valueOf(getTheSeconds()));
        }
    }

    public static long resetTimer() {
        TimerThread.seconds = 115;
        return TimerThread.seconds;
    }
    public static long getTheSeconds() {
        return TimerThread.seconds;
    }
}
