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

package io.github.hglabplh_tech.mines.backend.config;

public class Configuration {
    public static final String MINE_PLAYMODE_KEY =   "msweep.game.playmode";
    public static final String MINE_GRID_WIDTH_KEY = "msweep.grid.width";
    public static final String MINE_GRID_HEIGHT_KEY = "msweep.grid.height";
    public static final String MINE_COUNT_KEY = "msweep.mines.count";
    public static final String MINE_LEVELUP_KEY =   "msweep.levelup";
    public static final String MINE_READY_PERCENT_KEY = "msweep.ready.percent";

    public static ConfigBean configBean = null;

    public static ConfigBean getConfigBeanInstance () {
        if (configBean == null) {
            Configuration.configBean = new ConfigBean();
        }
        return Configuration.configBean;
    }


    public static class MineConfig {

    }
    public static class ConfigBean {
        private final MineConfig mineConfig = new MineConfig();

        public ConfigBean () {

        }
    }
}
