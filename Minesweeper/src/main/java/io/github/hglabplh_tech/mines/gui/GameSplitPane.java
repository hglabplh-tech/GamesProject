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

import javax.swing.*;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

public class GameSplitPane {
    private final JPanel statusPanel;
    private final JPanel playPanel;
    private final Integer splitMode;

    private final JSplitPane splitPane;


    public GameSplitPane(Integer splitMode, JPanel statusPanel, JPanel playPanel) {
        this.splitMode = splitMode;
        this.statusPanel = statusPanel;
        this.playPanel = playPanel;
        this.splitPane = new JSplitPane(getSplitMode(), getStatusPanel(), getPlayPanel());
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }

    public JPanel getPlayPanel() {
        return playPanel;
    }

    public Integer getSplitMode() {
        return splitMode;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }
}
