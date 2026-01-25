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
package io.github.hglabplh_tech.games.backend.util;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.net.URL;

public class GeneralUtils {

    /**
     * This method creates an ImageIcon out of a given valid graphics file like PNG,JPG,GIF
     * @param fname the filename of the icon to load
     * @return returns the loaded image-icon
     */
    public static ImageIcon createIcon(String fname) {
        URL imgURL = GeneralUtils.class.getResource("/images/" + fname);
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
            URL wavURL = GeneralUtils.class.getResource("/sounds/" + fname);
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
    public static void waitSeconds(float seconds) {
        try {
            long milliSeconds = (long)(seconds * 1000);
            Thread.sleep(milliSeconds);
        } catch (Exception e) {

        }
    }


}
