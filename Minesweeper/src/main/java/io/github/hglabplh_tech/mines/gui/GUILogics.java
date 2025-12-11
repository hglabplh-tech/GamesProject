package io.github.hglabplh_tech.mines.gui;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.time.Duration;

public class GUILogics {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI(final PlayModes mode) {

        //Create and set up the window.
        JFrame frame = new JFrame("Mine Sweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        // Create status Pane
        StatusPanel status = new StatusPanel(mode);
        //Create and set up the content pane.
        Sweeper playPane = new Sweeper(mode);

        GameSplitPane gamePane = new GameSplitPane(JSplitPane.VERTICAL_SPLIT, status, playPane);
        JSplitPane mainPane =gamePane.getSplitPane();

        status.setOpaque(true);
        playPane.setOpaque(true);

        frame.setContentPane(mainPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
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

    public static void playSound(String fname) {
        // Source - https://stackoverflow.com/a
// Posted by tschwab, modified by community. See post 'Timeline' for change history
// Retrieved 2025-11-29, License - CC BY-SA 3.0
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
        }
        catch (Exception e) {
            //whatevers
        }

    }

    public static void waitSeconds(int seconsds) {
        try {
            Duration duration = Duration.ofSeconds(seconsds);
            Thread.sleep(duration);
        } catch (Exception e) {

        }
    }
}
