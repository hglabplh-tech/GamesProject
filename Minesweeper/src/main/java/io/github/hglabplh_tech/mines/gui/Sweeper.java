/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package io.github.hglabplh_tech.mines.gui;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/* 
 * ButtonDemo.java requires the following files:
 *   images/right.gif
 *   images/middle.gif
 *   images/left.gif
 */
public class Sweeper extends JPanel
                        implements ActionListener {
    private final SweeperLogic util;
    private final ImageIcon mineIcon;
    private final ImageIcon bangIcon;
    private final ImageIcon waterIcon;
    private final ImageIcon questionIcon;
    private final ImageIcon startIcon;
    private final ImageIcon endIcon;
    private final ImageIcon baseoneIcon;
    private final ImageIcon basetwoIcon;
    private final List<JButton> buttonList = new ArrayList<>();
    private final PlayModes playMode;

    public Sweeper(PlayModes mode) {
        this.playMode = mode;
        this.mineIcon = GUILogics.createIcon("mine.png");
        this.bangIcon = GUILogics.createIcon("bang.gif");
        this.questionIcon = GUILogics.createIcon("question.jpeg");
        this.waterIcon = GUILogics.createIcon("water.png");
        this.startIcon = GUILogics.createIcon("start.jpg");
        this.baseoneIcon = GUILogics.createIcon("baseone.jpg");
        this.basetwoIcon = GUILogics.createIcon("basetwo.jpg");
        this.endIcon = GUILogics.createIcon("end.jpg");
        this.util = new SweeperLogic(mode, 15, 15, 30);
        List<List<SweeperLogic.ButtDescr>> array = util.calculateMines();
        GridLayout grid = new GridLayout();
        grid.setVgap(3);
        grid.setHgap(3);

        grid.setRows(this.util.getCy()+ 1);
        grid.setColumns(this.util.getCx());
        this.setLayout(grid);
        for (int y = 0; y < this.util.getCy() ;y++) {
            for (int x = 0; x < this.util.getCx() ;x++) {
                SweeperLogic.ButtDescr bDescr = array.get(y).get(x);
                makeAndAddButton(x, y, bDescr);
            }
        }

    }

    private void negativeEnd(String origName) {
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
                butt.setIcon(this.waterIcon);
            }
        });
        GUILogics.playSound("the-explosion.wav");
    }

    private void positiveEnd() {
        this.buttonList.forEach(butt ->
                butt.setIcon(this.waterIcon));
        GUILogics.playSound("winning-bell.wav");
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    private void makeAndAddButton(Integer x, Integer y, SweeperLogic.ButtDescr bDescr) {
        JButton button = null;
        if (playMode.equals(PlayModes.LABYRINTH)) {
          button = switch(bDescr.getPointType()) {
              case ENDPOINT ->  new JButton(this.endIcon);
              case FIRST_BASE -> new JButton(this.baseoneIcon);
              case SECOND_BASE -> new JButton(this.basetwoIcon);
              case STARTPOINT -> new JButton(this.startIcon);
              default -> new JButton(this.questionIcon);
          };
        } else {
            button = new JButton(this.questionIcon);
        }
        button.setName(this.util.makeButtonName(x,y, bDescr.isMine()));
        button.setLocation(x, y);
        button.setSize(5,5);
        button.setVisible(true);
        button.addActionListener(this);
        this.add(button);
        this.buttonList.add(button);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton)e.getSource();
        String name = source.getName();
        //source.setEnabled(false);
        if (this.util.isMineHit(name)) {
            source.setIcon(this.mineIcon);
            negativeEnd(source.getName());
        } else {

            source.setIcon(this.waterIcon);
            GUILogics.playSound("the-bell.wav");
        }
        if (util.isPositiveEnd()) {
            positiveEnd();
        }
    }

}
