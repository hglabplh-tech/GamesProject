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

import io.github.hglabplh_tech.games.backend.config.PlayModes;

import javax.swing.*;

public class Starter {

    /**
     * This is the starter class
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PlayModes mode = PlayModes.NORMAL;
        if (args.length == 1) {
            String argument = args[0];
            mode = switch (argument) {
                case "normal" -> PlayModes.NORMAL;
                case "lab" -> PlayModes.LABYRINTH;
                case "enhanced" -> PlayModes.ENHANCED;
                default -> PlayModes.NORMAL;
            };
        } else {
            mode = PlayModes.NORMAL;
        }
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        PlayModes finalMode = mode;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUILogics.createAndShowGUI(finalMode);
            }
        });
    }
}
