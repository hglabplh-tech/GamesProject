package io.github.hglabplh_tech.mines.gui;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;

import javax.swing.*;

public class Starter {

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
