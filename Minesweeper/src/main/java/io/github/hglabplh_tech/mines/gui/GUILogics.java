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
     * @param mode the play mode either LABYRINTH - NORMAL or ENHANCED
     */
    public static void createAndShowGUI(final PlayModes mode) {

        //Create and set up the window.
        JFrame frame = new JFrame("Mine Sweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        // Create status Pane
        StatusPanel status = new StatusPanel(mode);
        //Create and set up the content pane.
        Sweeper playPane = new Sweeper(mode, status);

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
        // Posted by tschwab, modified by community. See post 'Timeline' for change history
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
}
