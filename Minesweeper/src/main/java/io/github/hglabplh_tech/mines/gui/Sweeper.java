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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import io.github.hglabplh_tech.mines.backend.ButtonStatus;
import io.github.hglabplh_tech.mines.backend.ButtonPoint;
import io.github.hglabplh_tech.mines.backend.Labyrinth;
import io.github.hglabplh_tech.mines.backend.config.Configuration;
import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;
import io.github.hglabplh_tech.mines.backend.util.Point;

import static io.github.hglabplh_tech.mines.gui.GUILogics.createPopupMenu;

/*
 * ButtonDemo.java requires the following files:
 *   images/right.gif
 *   images/middle.gif
 *   images/left.gif
 */
public class Sweeper extends JPanel
        implements ActionListener {
    private SweeperLogic util;
    private final ImageIcon mineIcon;
    private final ImageIcon bangIcon;
    private final ImageIcon waterIcon;
    private final ImageIcon questionIcon;
    private final ImageIcon startIcon;
    private final ImageIcon endIcon;
    private final ImageIcon baseoneIcon;
    private final ImageIcon basetwoIcon;
    private final ImageIcon purpleIcon;
    private final ImageIcon pathIcon;
    private final List<JButton> buttonList = new ArrayList<>();
    private PlayModes playMode;
    private final StatusPanel statusPanel;


    public static Sweeper instance;
    private Labyrinth labyrinth;

    public Sweeper(Configuration.ConfigBean configBean, StatusPanel panel) {
        instance = this;
        this.playMode = configBean.getMineConfig().getPlayMode();
        this.statusPanel = panel;
        this.mineIcon = GUILogics.createIcon("mine.png");
        this.bangIcon = GUILogics.createIcon("bang.png");
        this.questionIcon = GUILogics.createIcon("question.png");
        this.waterIcon = GUILogics.createIcon("ok.png");
        this.startIcon = GUILogics.createIcon("start.png");
        this.baseoneIcon = GUILogics.createIcon("baseone.png");
        this.basetwoIcon = GUILogics.createIcon("basetwo.png");
        this.endIcon = GUILogics.createIcon("finish.png");
        this.purpleIcon = GUILogics.createIcon("crazy.png");
        this.pathIcon = GUILogics.createIcon("path.png");
        JPopupMenu popupMenu = createPopupMenu();
        this.add(popupMenu);
        this.addMouseListener(new GameMouseListener(popupMenu));
        initButtons(configBean);

    }

    public static Sweeper getSweeper(PlayModes mode) {
        instance.playMode = mode;
        return instance;
    }

    public void initButtons(Configuration.ConfigBean configBean) {
        this.invalidate();
        this.removeAll();
        this.statusPanel.resetCounterValueAndScore(this.playMode);
        this.util = new SweeperLogic(this.statusPanel.getPlayMode(),
                configBean.getMineConfig().getGridCX(),
                configBean.getMineConfig().getGridCY(),
                configBean.getMineConfig().getMinesCount());
        List<List<ButtonStatus>> fieldsArray = buildFieldsArray();
        GridLayout grid = new GridLayout();
        grid.setVgap(3);
        grid.setHgap(3);

        grid.setRows(this.util.getCy() + 1);
        grid.setColumns(this.util.getCx());
        this.setLayout(grid);
        for (int y = 0; y < this.util.getCy(); y++) {
            for (int x = 0; x < this.util.getCx(); x++) {
                ButtonStatus bDescr = fieldsArray.get(y).get(x);
                makeAndAddButton(x, y, bDescr);
            }
        }
        this.repaint();
    }

    public List<List<ButtonStatus>> buildFieldsArray() {
        List<List<ButtonStatus>> array = util.calculateMines();
        if (this.playMode.equals(PlayModes.LABYRINTH)) {
            buildLabyrinth();
        }
        return array;
    }

    private void buildLabyrinth() {
        Labyrinth labyrinth = this.util.getLabyrinth();
        if (labyrinth != null) {
            this.labyrinth = labyrinth;
        } else {
            throw new IllegalStateException("Labyrinth object should be there");
        }
        this.labyrinth.addToPath(this.labyrinth.getStart());

    }

    private void negativeEnd(String origName) {
        StatusPanel.getTimerThread().stop();
        String[] origValues = origName.split("#");

        this.buttonList.forEach(butt -> {
            String theName = butt.getName();
            String[] values = theName.split("#");
            if (Boolean.valueOf(values[2])) {
                if (util.compNamesXY(theName, origName)) {
                    butt.setIcon(this.bangIcon);
                } else {
                    butt.setIcon(this.mineIcon);
                }
            } else {
                if (this.playMode.equals(PlayModes.LABYRINTH)) {
                    switch (this.util.extractPointType(theName)) {
                        case STARTPOINT -> butt.setIcon(this.startIcon);
                        case ENDPOINT -> butt.setIcon(this.endIcon);
                        case FIRST_BASE -> butt.setIcon(this.baseoneIcon);
                        case SECOND_BASE -> butt.setIcon(this.basetwoIcon);
                        default -> {
                            if (util.compNamesXY(origName, theName)) {
                                butt.setIcon(this.purpleIcon);
                            } else {
                                butt.setIcon(this.waterIcon);
                            }
                        }
                    }

                } else {
                    butt.setIcon(this.waterIcon);
                }
            }
        });
        GUILogics.playSound("the-explosion.wav");
    }

    private void labTimerOutThread(JButton source) {
        if (this.playMode.equals(PlayModes.LABYRINTH)) {
            if (TimerThread.getTheSeconds() <= 0) {
                negativeEnd(source.getName());
            }
        }
    }

    private void positiveEnd() {
        StatusPanel.getTimerThread().stop();
        this.buttonList.forEach(butt ->
                butt.setIcon(this.waterIcon));
        GUILogics.playSound("winning-bell.wav");
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    private void makeAndAddButton(Integer x, Integer y, ButtonStatus bDescr) {
        JButton button = null;
        if (playMode.equals(PlayModes.LABYRINTH)) {
            button = switch (bDescr.pointType()) {
                case ENDPOINT -> new JButton(this.endIcon);
                case FIRST_BASE -> new JButton(this.baseoneIcon);
                case SECOND_BASE -> new JButton(this.basetwoIcon);
                case STARTPOINT -> new JButton(this.startIcon);
                default -> new JButton(this.questionIcon);
            };
        } else {
            button = new JButton(this.questionIcon);
        }
        button.setName(this.util.makeButtonName(x, y, bDescr.isMine()));
        button.setLocation(x, y);
        button.setSize(5, 5);
        button.setVisible(true);
        button.addActionListener(this);
        this.add(button);
        this.buttonList.add(button);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        labTimerOutThread(source);
        String name = source.getName();

        //source.setEnabled(false);
        if (this.util.isMineHit(name)) {
            source.setIcon(this.mineIcon);
            negativeEnd(source.getName());
        } else {
            if (this.playMode.equals(PlayModes.LABYRINTH)) {
                ButtonPoint nextPoint = this.util.extractPointFromName(name);
                this.labyrinth.addToPath(nextPoint);
                if (!labyrinth.checkStepOrder()) {
                    source.setIcon(this.purpleIcon);
                    source.invalidate();
                    GUILogics.playSound("alarm.wav");
                    GUILogics.playSound("the-explosion.wav");
                    GUILogics.waitSeconds(5L);
                    negativeEnd(name);
                } else {
                    source.setIcon(this.waterIcon);
                }

            } else {
                source.setIcon(this.waterIcon);
            }
            GUILogics.playSound("the-bell.wav");
        }
        boolean positiveEnd = false;
        if (this.playMode.equals(PlayModes.NORMAL)) {
            positiveEnd = this.util.isPositiveEnd();
        } else if (this.playMode.equals(PlayModes.LABYRINTH)) {
            positiveEnd = labyrinth.isPositiveEnd();
        } else {
            positiveEnd = false;
        }
        if (positiveEnd) {
            positiveEnd();
        }
        if (this.playMode.equals(PlayModes.LABYRINTH)) {
            this.statusPanel.decrementCounterValue();
            this.statusPanel.incrementScoreValueBy(1);
            Point actPoint = this.util.getNamesPoint(name);
            if (labyrinth.getNextPoint().equalsInPoint(actPoint)) {
                ButtonPoint nextPoint = labyrinth.switchToNextBase(actPoint.x(), actPoint.y());
                int actScore = Integer.parseInt(statusPanel.getScoreValue().getText());
                this.statusPanel.incrementScoreValueBy((actScore * 2));
            }
        } else {
            this.statusPanel.incrementCounterValue();
            this.statusPanel.incrementScoreValueBy(1);
        }

    }

}