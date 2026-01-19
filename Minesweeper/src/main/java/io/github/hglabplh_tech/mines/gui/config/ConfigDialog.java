package io.github.hglabplh_tech.mines.gui.config;

import io.github.hglabplh_tech.games.backend.config.ConfigUtil;
import io.github.hglabplh_tech.games.backend.config.Configuration.ConfigBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ConfigDialog extends JTabbedPane implements ActionListener, PropertyChangeListener {


    private final BaseTab baseTab;

    public ConfigDialog(ConfigBean configBean) {
        this.baseTab = new BaseTab(configBean);
        this.addTab("Minesweeper Base Config", this.baseTab);
        this.baseTab.setVisible(true);
        this.baseTab.setBackground(Color.magenta);

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public class BaseTab extends JPanel implements ActionListener {

        private final ConfigBean configBean;

        private final JTextField cxField;
        private final JTextField cyField;
        private final JTextField thePlayMode;
        private final JTextField minesCount;

        private final JTextField cxFieldValue;
        private final JTextField cyFieldValue;
        private final JTextField playModeValue;
        private final JTextField minesCountValue;

        private final JButton changeButton;
        private final JButton saveButton;
        private final JButton cancelChangeButton;



        public BaseTab(ConfigBean configBean) {
            this.configBean = configBean;
            this.thePlayMode = new JTextField("PlayMode:");
            this.playModeValue = new JTextField(this.configBean.mineConfig().getPlayMode().getPlayModeName());
            //this.playModeValue.setEditable(true);
            this.cxField = new JTextField("CX-Items:");
            this.cxFieldValue = new JTextField(this.configBean.mineConfig().getGridCX());
            this.cyField = new JTextField("CY-Items:");
            this.cyFieldValue = new JTextField(this.configBean.mineConfig().getGridCY());
            this.minesCount = new JTextField("Mines count:");
            this.minesCountValue = new JTextField(this.configBean.mineConfig().getMinesCount());

            this.setAllFieldsEditability(false);

            this.changeButton = new JButton("Change Config");
            this.changeButton.setBackground(Color.green);
            this.changeButton.setActionCommand("changeconfig");
            this.changeButton.addActionListener(this);

            this.saveButton = new JButton("Save Configuration");
            this.saveButton.setBackground(Color.green);
            this.saveButton.setActionCommand("saveconfig");
            this.saveButton.addActionListener(this);

            this.cancelChangeButton = new JButton("Cancel Change Configuration");
            this.cancelChangeButton.setBackground(Color.green);
            this.cancelChangeButton.setActionCommand("cancelchangeconfig");
            this.cancelChangeButton.addActionListener(this);

            this.add(this.thePlayMode);
            this.add(this.playModeValue);
            this.add(this.cxField);
            this.add(this.cxFieldValue);
            this.add(this.cyField);
            this.add(this.cyFieldValue);
            this.add(this.minesCount);
            this.add(this.minesCountValue);

            this.add(this.changeButton);
            this.add(this.saveButton);
            this.add(this.cancelChangeButton);

        }

        public ConfigBean configBean() {
            return this.configBean;
        }

        public JTextField cxField() {
            return this.cxField;
        }

        public JTextField cyField() {
            return this.cyField;
        }

        public JTextField cxFieldValue() {
            return this.cxFieldValue;
        }

        public JTextField cyFieldValue() {
            return this.cyFieldValue;
        }

        public JTextField thePlayMode() {
            return this.thePlayMode;
        }

        public JTextField playModeValue() {
            return this.playModeValue;
        }

        public JTextField minesCount() {
            return this.minesCount;
        }

        public JTextField minesCountValue() {
            return this.minesCountValue;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {
                JButton source = (JButton)e.getSource();
                String command = e.getActionCommand();

                if (command.equals("changeconfig")) {
                    this.setAllFieldsEditability(true);
                } else if (command.equals("saveconfig")) {
                    this.setAllFieldsEditability(false);
                    ConfigUtil.saveUserProps(this.configBean.beanProperties());
                } else if (command.equals("cancelchangeconfig")) {
                    this.setAllFieldsEditability(false);
                }
            }
        }

        private void setAllFieldsEditability(boolean editable) {
            this.playModeValue().setEditable(editable);
            this.cxFieldValue().setEditable(editable);
            this.cyFieldValue().setEditable(editable);
            this.minesCountValue().setEditable(editable);
        }

    }
}
