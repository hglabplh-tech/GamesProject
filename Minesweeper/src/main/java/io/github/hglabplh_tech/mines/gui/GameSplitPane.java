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
