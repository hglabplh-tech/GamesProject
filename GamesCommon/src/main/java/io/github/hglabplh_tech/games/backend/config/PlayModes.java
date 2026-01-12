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
package io.github.hglabplh_tech.games.backend.config;


/**
 * Enum for the possible play-modes
 */
public enum PlayModes {
    NONE("none"),
    NORMAL("normal"), // the classic mode
    LABYRINTH("labyrinth"), // the labyrinth mode cudo's to Joshua Glab for the idea
    ENHANCED("enhanced"), // the mode enhanced here we have to look where it ends
    ;

    final String playModeName;
    PlayModes(String playModeName) {
        this.playModeName = playModeName;
    }

    public String getPlayModeName() {
        return this.playModeName;
    }

    public static PlayModes getPlayModeByName(String playModeName) {
        for (PlayModes mode  : PlayModes.values()) {
            if (mode.getPlayModeName().equals(playModeName)) {
                return mode;
            }
        }
        return PlayModes.NONE;
    }
}
