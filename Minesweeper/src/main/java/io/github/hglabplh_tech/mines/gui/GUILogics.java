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

import io.github.hglabplh_tech.games.backend.config.Configuration;
import io.github.hglabplh_tech.games.backend.config.PlayModes;

import javax.swing.*;


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
        JPopupMenu popupMenu = createPopupMenu();
        frame.add(popupMenu);
        frame.addMouseListener(new GameMouseListener(popupMenu));

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


    public static JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenu menu = createSubMenus();
        popupMenu.add(menu);
        return popupMenu;

    }
    public static JMenuBar createMenu() {
        JMenuBar menuBar;

        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        menuBar = new JMenuBar();

        JMenu menu = createSubMenus();
        menuBar.add(menu);
        return menuBar;

    }

    private static JMenu createSubMenus() {
        JMenu menu;
        JMenu submenu;
        menu = new JMenu("Game Actions");
        submenu = new JMenu("Save Actions");
        menu.add(submenu);

        MenuActionListener actionListener = new MenuActionListener();
        JMenuItem saveItem = new JMenuItem("Make a Save", VK_S);
        saveItem.setActionCommand("msave");
        saveItem.setMnemonic(VK_S);
        saveItem.addActionListener(actionListener);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(VK_S, ALT_DOWN_MASK));
        submenu.add(saveItem);
        JMenuItem loadItem = new JMenuItem("Load a Save", VK_L);
        loadItem.setAccelerator(KeyStroke.getKeyStroke(VK_L, ALT_DOWN_MASK));
        loadItem.setMnemonic(VK_L);
        loadItem.setActionCommand("lsave");
        loadItem.addActionListener(actionListener);
        submenu.add(loadItem);
        return menu;
    }
}
