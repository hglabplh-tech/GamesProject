package io.github.hglabplh_tech.mines.gui;

import javax.swing.*;
import java.awt.event.*;

public class MenuActionListener implements ActionListener,
        ItemListener, KeyListener, MouseListener {

    public MenuActionListener() {

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem src = (JMenuItem)e.getSource();
        String command = e.getActionCommand();
        if (command.equals("msave")) {
            System.out.println("Save Something");
        } else if (command.equals("lsave")) {
            System.out.println("Load Something");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object  item = e.getSource();
        if (item instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem)item;

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
