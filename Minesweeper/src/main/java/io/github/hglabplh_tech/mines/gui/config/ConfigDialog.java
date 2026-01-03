package io.github.hglabplh_tech.mines.gui.config;

import io.github.hglabplh_tech.mines.backend.config.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ConfigDialog extends JTabbedPane implements ActionListener, PropertyChangeListener {


    private final BaseTab baseTab;

    public ConfigDialog(Configuration config) {
        this.baseTab = new BaseTab(config);
        this.addTab("Base Config", this.baseTab);

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public class BaseTab extends JPanel {

        private final Configuration config;

        private final JTextField cxField;
        private final JTextField cyField;


        public BaseTab(Configuration config) {
            this.config = config;

            this.cxField = new JTextField("CX-Items");
            this.cyField = new JTextField("CY-Items");
            this.add(this.cxField);
            this.add(this.cyField);
        }
    }
}
