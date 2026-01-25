/*
Copyright (c) 2025/2026 Harald Glab-Plhak

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

import io.github.hglabplh_tech.games.backend.config.Configuration;
import io.github.hglabplh_tech.games.backend.config.PlayModes;
import io.github.hglabplh_tech.games.backend.util.GeneralUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static io.github.hglabplh_tech.mines.gui.GUILogics.createPopupMenu;

public class StatusPanel extends JPanel implements ActionListener {

    private final JLabel timeValue;
    private final JLabel counterValue;
    private final JLabel scoreValue;
    private final JLabel minesCountValue;

    private  PlayModes playMode;

    private final ButtonGroup radioGroup;
    private final JRadioButton radioButtNorm;
    private final JRadioButton radioButtLab;
    private final JRadioButton radioButtEnhanced;

    private final JButton showPathButton;
    private final JButton restartButton;
    private final JButton saveButton;

    private static Thread timerThread;

    public StatusPanel(Configuration.ConfigBean configBean) {

        this.playMode = configBean.mineConfig().getPlayMode();

        JLabel timerLabel = new JLabel("Time elapsed: ");
        this.timeValue = new JLabel("00");
        JLabel counterLabel = new JLabel("Counter: ");
        this.counterValue = new JLabel("0");
        JLabel scoreLabel = new JLabel("Score: ");
        this.scoreValue = new JLabel("0");
        JLabel minesCountLabel = new JLabel("Mines count: ");
        this.minesCountValue = new JLabel(configBean.mineConfig().getMinesCount().toString());

        this.restartButton = new JButton("Restart Game");
        this.restartButton.setBackground(Color.green);
        this.restartButton.setActionCommand("restart");
        this.restartButton.addActionListener(this);
        this.saveButton = new JButton("Save Game");
        this.saveButton.setBackground(Color.green);
        this.saveButton.setActionCommand("savegame");
        this.saveButton.addActionListener(this);

        this.showPathButton = new JButton("Show Paths");
        this.showPathButton.setVisible(false);
        this.showPathButton.setBackground(Color.green);
        this.showPathButton.setActionCommand("showPaths");
        this.showPathButton.addActionListener(this);

        this.radioGroup = new ButtonGroup();

        this.radioButtNorm = new JRadioButton("Normal", true);
        this.radioButtLab = new JRadioButton("Labyrinth", false);
        this.radioButtEnhanced = new JRadioButton("Enhanced", false);

        this.radioGroup.add(this.radioButtNorm);
        this.radioButtNorm.setActionCommand("nor");
        this.radioButtNorm.addActionListener(this);

        this.radioGroup.add(this.radioButtLab);
        this.radioButtLab.setActionCommand("lab");
        this.radioButtLab.addActionListener(this);

        this.radioGroup.add(this.radioButtEnhanced);
        this.radioButtEnhanced.setActionCommand("enh");
        this.radioButtEnhanced.addActionListener(this);

        this.add(timerLabel);
        this.add(this.timeValue);
        this.add(counterLabel);
        this.add(this.counterValue);
        this.add(scoreLabel);
        this.add(this.scoreValue);
        this.add(scoreLabel);
        this.add(this.scoreValue);
        this.add(minesCountLabel);
        this.add(this.minesCountValue);

        this.add(this.radioButtNorm);
        this.add(this.radioButtLab);
        this.add(this.radioButtEnhanced);
        this.add(showPathButton);
        switchShowButtonVisibility();
        this.add(restartButton);
        this.add(saveButton);
        timerThread = new Thread(new TimerThread(this));
        timerThread.start();
        switch(this.playMode) {
            case LABYRINTH -> this.radioButtLab.setSelected(true);
            case ENHANCED -> this.radioButtEnhanced.setSelected(true);
            default ->  this.radioButtNorm.setSelected(true);
        }
        JPopupMenu popupMenu = createPopupMenu();
        this.add(popupMenu);
        this.addMouseListener(new GameMouseListener(popupMenu));

    }

    public void switchShowButtonVisibility() {
        boolean pathButtonVisible = (this.playMode.equals(PlayModes.LABYRINTH));
        this.showPathButton.setVisible(pathButtonVisible);
    }

    public JLabel getTimerLabel() {
        return this.timeValue;
    }

    public JLabel getCounterLabel() {
        return this.counterValue;
    }

    public static Thread getTimerThread() {
        return timerThread;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JRadioButton || e.getSource() instanceof JButton) {
            boolean showPaths = false;
            this.getCounterValue().setText("0");
            timerThread.stop();
            timerThread = new Thread(new TimerThread(this));
            GeneralUtils.waitSeconds(1);
            TimerThread.resetTimer();
            timerThread.start();
            String command = e.getActionCommand();
            System.out.println(command);
            Sweeper sweeper = null;
            if (command.equals("nor")) {
                sweeper = Sweeper.getSweeper(PlayModes.NORMAL);
                this.playMode = PlayModes.NORMAL;
            } else if (command.equals("lab")) {
                sweeper = Sweeper.getSweeper(PlayModes.LABYRINTH);
                this.playMode = PlayModes.LABYRINTH;
            } else if (command.equals("enh")) {
                sweeper = Sweeper.getSweeper(PlayModes.NORMAL); // See todo ->
                this.playMode = PlayModes.NORMAL;//TODO: change this
                System.err.println("Enhanced has to be implemented");
            } else if (command.equals("restart")) {
                sweeper = Sweeper.getSweeper(this.playMode); //TODO: change this
            } else if (command.equals("showPaths")) {
                showPaths = (this.playMode.equals(PlayModes.LABYRINTH));
                sweeper = Sweeper.getSweeperInst(this.playMode);
            }
            if (showPaths) {
                sweeper.showPaths();
            } else {
                sweeper.initButtons(Configuration.getConfigBeanInstance());
            }

        }
    }

    public JLabel getCounterValue() {
        return this.counterValue;
    }

    public JLabel getScoreValue() {
        return this.scoreValue;
    }


    public void incrementCounterValue() {
        Integer value = Integer.valueOf(getCounterValue().getText());
        value++;
        getCounterValue().setText(value.toString());
    }

    public void incrementScoreValueBy(int increment) {
        Integer value = Integer.valueOf(getScoreValue().getText());
        value = value + increment;
        getScoreValue().setText(value.toString());
    }

    public void resetCounterValueAndScore(PlayModes mode) {
        if (mode.equals(PlayModes.LABYRINTH)) {
            getCounterValue().setText("1500");
        } else {
            getCounterValue().setText("0");
        }
        getScoreValue().setText("0");
    }

    public void decrementCounterValue() {
        Integer value = Integer.valueOf(getCounterValue().getText());
        value--;
        getCounterValue().setText(value.toString());
    }

    public PlayModes getPlayMode() {
        return playMode;
    }

    public JLabel getTimeValue() {
        return timeValue;
    }

    public ButtonGroup getRadioGroup() {
        return radioGroup;
    }

    public JRadioButton getRadioButtNorm() {
        return radioButtNorm;
    }

    public JRadioButton getRadioButtLab() {
        return radioButtLab;
    }

    public JRadioButton getRadioButtEnhanced() {
        return radioButtEnhanced;
    }

    public boolean normIsSelected() {
        return getRadioButtNorm().isSelected();
    }

    public boolean labIsSelected() {
        return getRadioButtLab().isSelected();
    }
    public boolean enhancedIsSelected() {
        return getRadioButtEnhanced().isSelected();
    }

    public PlayModes playModeSelection() {
        if (normIsSelected()) {
            this.playMode = PlayModes.NORMAL;
        } else if (labIsSelected()) {
            this.playMode = PlayModes.LABYRINTH;
        } else if (enhancedIsSelected()) {
            this.playMode = PlayModes.ENHANCED;
        } else {
            this.playMode =  PlayModes.NORMAL;
        }
        return this.playMode;
    }

}
