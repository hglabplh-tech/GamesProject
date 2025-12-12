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

import java.util.Optional;
import java.util.Properties;

import static io.github.hglabplh_tech.mines.backend.config.ConfigUtil.getConfigValue;

public class Configuration {
    public static final String MINES_PLAYMODE_KEY =   "msweep.game.playmode";
    public static final String MINES_PLAYMODE_VAL = "normal";

    public static final String MINES_GRID_WIDTH_KEY = "msweep.grid.width";
    public static final String MINES_GRID_WIDTH_VAL = "20";

    public static final String MINES_GRID_HEIGHT_KEY = "msweep.grid.height";
    public static final String MINES_GRID_HEIGHT_VAL = "20";

    public static final String MINES_COUNT_KEY = "msweep.mines.count";
    public static final String MINES_COUNT_VAL = "45";

    public static final String MINES_LEVELUP_KEY =   "msweep.levelup";
    public static final String MINES_LEVELUP_VAL =   "false";

    public static final String MINES_READY_PERCENT_KEY = "msweep.ready.percent";
    public static final String MINES_READY_PERCENT_VAL = "false";

    public static ConfigBean configBean = null;

    public static ConfigBean getConfigBeanInstance () {
        if (configBean == null) {
            Configuration.configBean = new ConfigBean();
        }
        return Configuration.configBean;
    }


    public static class MineConfig {
        private final PlayModes playMode;

        private final Integer gridCX;

        private final Integer gridCY;

        private final Integer minesCount;

        private final Boolean levelUp;

        private final Boolean stopAterPercent;

        public MineConfig() {
            Optional<Properties> props = ConfigUtil.loadUserProps();
            Optional<String> optValue = ConfigUtil.getConfigValue(MINES_PLAYMODE_KEY, MINES_PLAYMODE_VAL);
            this.playMode = PlayModes.getPlayModeByName(optValue.orElse(MINES_PLAYMODE_VAL));

            optValue = ConfigUtil.getConfigValue(MINES_GRID_WIDTH_KEY, MINES_GRID_WIDTH_VAL);
            this.gridCX = Integer.valueOf(optValue.orElse(MINES_GRID_WIDTH_VAL));

            optValue = ConfigUtil.getConfigValue(MINES_GRID_HEIGHT_KEY, MINES_GRID_HEIGHT_VAL);
            this.gridCY = Integer.valueOf(optValue.orElse(MINES_GRID_HEIGHT_VAL));

            optValue = ConfigUtil.getConfigValue(MINES_COUNT_KEY, MINES_COUNT_VAL);
            this.minesCount = Integer.valueOf(optValue.orElse(MINES_COUNT_VAL));

            optValue = ConfigUtil.getConfigValue(MINES_LEVELUP_KEY, MINES_LEVELUP_VAL);
            this.levelUp = Boolean.valueOf(optValue.orElse(MINES_LEVELUP_VAL));

            optValue = ConfigUtil.getConfigValue(MINES_READY_PERCENT_KEY, MINES_READY_PERCENT_VAL);
            this.stopAterPercent = Boolean.valueOf(optValue.orElse(MINES_READY_PERCENT_VAL));

            Properties toSave = props.orElse(new Properties());

            toSave.setProperty(MINES_PLAYMODE_KEY,this.playMode.getPlayModeName());

            toSave.setProperty(MINES_GRID_WIDTH_KEY,String.valueOf(this.gridCX));

            toSave.setProperty(MINES_GRID_HEIGHT_KEY, String.valueOf(this.gridCY));

            toSave.setProperty(MINES_COUNT_KEY, String.valueOf(this.minesCount));

            toSave.setProperty(MINES_LEVELUP_KEY, String.valueOf(this.levelUp));

            toSave.setProperty(MINES_LEVELUP_KEY, String.valueOf(this.stopAterPercent));

            ConfigUtil.saveUserProps(toSave);
        }

        public PlayModes getPlayMode() {
            return playMode;
        }

        public Integer getGridCX() {
            return gridCX;
        }

        public Integer getGridCY() {
            return gridCY;
        }

        public Integer getMinesCount() {
            return minesCount;
        }

        public Boolean getLevelUp() {
            return levelUp;
        }

        public Boolean getStopAterPercent() {
            return stopAterPercent;
        }
    }


    public static class ConfigBean {
        private final MineConfig mineConfig;

        public ConfigBean () {
            this.mineConfig = new MineConfig();
        }

        public MineConfig getMineConfig() {
            return mineConfig;
        }
    }
}
