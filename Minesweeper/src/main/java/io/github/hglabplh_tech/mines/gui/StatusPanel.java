package io.github.hglabplh_tech.mines.gui;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusPanel extends JPanel implements ActionListener {

    private final JLabel timeValue;
    private final JLabel counterValue;

    private  PlayModes playMode;

    private final ButtonGroup radioGroup;
    private final JRadioButton radioButtNorm;
    private final JRadioButton radioButtLab;
    private final JRadioButton radioButtEnhanced;

    public StatusPanel(PlayModes playMode) {

        this.playMode = playMode;

        JLabel timerLabel = new JLabel("Time elapsed: ");
        this.timeValue = new JLabel("00:00");
        JLabel counterLabel = new JLabel("Counter: ");
        this.counterValue = new JLabel("0");

        this.radioGroup = new ButtonGroup();

        this.radioButtNorm = new JRadioButton("Normal", true);
        this.radioButtLab = new JRadioButton("Labyrinth", false);
        this.radioButtEnhanced = new JRadioButton("Enhanced", false);

        this.radioGroup.add(this.radioButtNorm);
        this.radioGroup.add(this.radioButtLab);
        this.radioGroup.add(this.radioButtEnhanced);

        this.add(timerLabel);
        this.add(this.timeValue);
        this.add(counterLabel);
        this.add(this.counterValue);

        this.add(this.radioButtNorm);
        this.add(this.radioButtLab);
        this.add(this.radioButtEnhanced);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public JLabel getCounterValue() {
        return counterValue;
    }

    public void incrementCounterValue() {
        Integer value = Integer.valueOf(getCounterValue().getText());
        value++;
        getCounterValue().setText(value.toString());
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
