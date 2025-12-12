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

import io.github.hglabplh_tech.mines.backend.config.Configuration;
import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.time.Duration;

import static java.awt.event.KeyEvent.*;

public class GUILogics {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     * @param mode the play mode either LABYRINTH - NORMAL or ENHANCED
     */
    public static void createAndShowGUI(final PlayModes mode) {

        //Create and set up the window.
        JFrame frame = new JFrame("Mine Sweeper");
        frame.add(createPopupMenu());
        frame.setJMenuBar(createMenu());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        Configuration.ConfigBean configBean = Configuration.getConfigBeanInstance();
        // Create status Pane
        StatusPanel status = new StatusPanel(configBean);
        //Create and set up the content pane.
        Sweeper playPane = new Sweeper(configBean, status);

        GameSplitPane gamePane = new GameSplitPane(JSplitPane.VERTICAL_SPLIT, status, playPane);
        JSplitPane mainPane =gamePane.getSplitPane();

        status.setOpaque(true);
        playPane.setOpaque(true);

        frame.setContentPane(mainPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * This method creates an ImageIcon out of a given valid graphics file like PNG,JPG,GIF
     * @param fname the filename of the icon to load
     * @return returns the loaded image-icon
     */
    public static ImageIcon createIcon(String fname) {
        java.net.URL imgURL = SweeperLogic.class.getResource("/images/" + fname);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            return icon;
        } else {
            System.err.println("Couldn't find file: " + "/images/" +fname);
            return null;
        }
    }

    /**
     * This procedure / method plays a sound from a given source at the moment only WAV I will change this
     * @param fname the filename of the WAV file
     */
    public static Clip playSound(String fname) {
        // Source - https://stackoverflow.com/a
        // Po0sted by tschwab, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-11-29, License - CC BY-SA 3.0 - used with changes
        try {
            java.net.URL wavURL = SweeperLogic.class.getResource("/sounds/" + fname);
            File yourFile = new File(wavURL.toURI());
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;

            stream = AudioSystem.getAudioInputStream(yourFile);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            return clip;
        }
        catch (Exception e) {
            throw new IllegalStateException("clip cannot be played exception occurred");
        }

    }

    /**
     * This method simply waits for the given count of seconds
     * @param seconds seconds to wait
     */
    public static void waitSeconds(long seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (Exception e) {

        }
    }

    public static JPopupMenu createPopupMenu() {
        JPopupMenu menuBar;
        JMenu menu, submenu;

        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        menuBar = new JPopupMenu();
        menu = new JMenu("Game Actions");
        submenu = new JMenu("Save Actions");
        menu.add(submenu);
        JMenuItem saveItem = new JMenuItem("Make a Save", 1);
        submenu.add(saveItem);
        JMenuItem loadItem = new JMenuItem("Load a Save", 1);
        submenu.add(loadItem);
        menuBar.add(menu);
        return menuBar;

    }
    public static JMenuBar createMenu() {
        JMenuBar menuBar;
        JMenu menu, submenu;

        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("Game Actions");
        submenu = new JMenu("Save Actions");
        menu.add(submenu);

        MenuActionListener actionListener = new MenuActionListener();
        JMenuItem saveItem = new JMenuItem("Save", VK_S);
        saveItem.setMnemonic(VK_S);
        saveItem.addActionListener(actionListener);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(VK_S, ALT_DOWN_MASK));
        submenu.add(saveItem);
        JMenuItem loadItem = new JMenuItem("Load", VK_L);
        loadItem.addActionListener(actionListener);
        submenu.add(loadItem);
        menuBar.add(menu);
        return menuBar;

    }
}
