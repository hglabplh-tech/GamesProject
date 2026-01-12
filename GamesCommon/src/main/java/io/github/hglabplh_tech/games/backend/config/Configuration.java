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

import java.util.Optional;
import java.util.Properties;

public class Configuration {
    public static final String LOG_CONFIG_PATH_KEY =   "log.config.path";
    public static final String LOG_CONFIG_PATH_VAL;

    public static final String LOG_MIN_LEVEL_KEY = "log.mindebug.level";
    public static final String LOG_MIN_LEVEL_VAL = "debug";

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


    static {
        LOG_CONFIG_PATH_VAL =ConfigUtil.LOG_PROP_FILE.getAbsolutePath();
    }
    public static ConfigBean getConfigBeanInstance () {
        if (configBean == null) {
            Configuration.configBean = new ConfigBean();
        }
        return Configuration.configBean;
    }

    public static class LogConfig {

        private String configPath;

        private String minLevel;

        public LogConfig(String configPath, String minLevel) {
            this.configPath = configPath;
            this.minLevel = minLevel;
        }

        public LogConfig() {
            Optional<String> optValue = ConfigUtil.getConfigValue(LOG_CONFIG_PATH_KEY, LOG_CONFIG_PATH_VAL);
            this.configPath = optValue.orElse(LOG_CONFIG_PATH_VAL);

            optValue = ConfigUtil.getConfigValue(LOG_MIN_LEVEL_KEY, LOG_MIN_LEVEL_VAL);
            this.minLevel = optValue.orElse(LOG_MIN_LEVEL_VAL);
        }

        public String configPath() {
            return configPath;
        }

        public String minLevel() {
            return minLevel;
        }

        public LogConfigBuilder copyBuilder() {
            return new LogConfigBuilder(this);
        }

        public static class LogConfigBuilder {

            private LogConfig logConfig;

            public LogConfigBuilder(LogConfig config) {
                this.logConfig = new LogConfig(config.configPath(), config.minLevel());
            }

            public LogConfigBuilder configPath(String configPath) {
                logConfig.configPath = configPath;
                return this;
            }

            public LogConfigBuilder minLevel(String minLevel) {
                logConfig.minLevel = minLevel;
                return this;
            }

            public LogConfig build() {
                return this.logConfig;
            }

        }
    }


    public static class MineConfig {
        private PlayModes playMode;

        private Integer gridCX;

        private Integer gridCY;

        private Integer minesCount;

        private Boolean levelUp;

        private Boolean stopAfterPercent;

        public MineConfig(final PlayModes playMode,final Integer gridCX,final Integer gridCY,
                          final Integer minesCount, final Boolean levelUp, final Boolean stopAfterPercent) {
            this.playMode = playMode;
            this.gridCX = gridCX;
            this.gridCY = gridCY;
            this.minesCount = minesCount;
            this.levelUp = levelUp;
            this.stopAfterPercent = stopAfterPercent;
        }
        public MineConfig() {
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
            this.stopAfterPercent = Boolean.valueOf(optValue.orElse(MINES_READY_PERCENT_VAL));


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

        public Boolean getStopAfterPercent() {
            return stopAfterPercent;
        }

        public MineConfigBuilder copyBuilder() {
            return new MineConfigBuilder(this);
        }

        public static class MineConfigBuilder {

            private MineConfig mineConfig;

            public MineConfigBuilder(MineConfig config) {
                this.mineConfig = new MineConfig(config.getPlayMode(), config.getGridCX(),
                        config.getGridCY(), config.getMinesCount(), config.getLevelUp(),
                        config.getStopAfterPercent());

            }

            public MineConfigBuilder playMode(PlayModes mode) {
                mineConfig.playMode = mode;
                return this;
            }

            public MineConfigBuilder gridCX(Integer cx) {
                mineConfig.gridCX = cx;
                return this;
            }

            public MineConfigBuilder gridCY(Integer cy) {
                mineConfig.gridCY = cy;
                return this;
            }

            public MineConfigBuilder minesCount(Integer minesCount) {
                mineConfig.minesCount = minesCount;
                return this;
            }

            public MineConfigBuilder levelUp(Boolean levelUp) {
                mineConfig.levelUp = levelUp;
                return this;
            }

            public MineConfigBuilder stopAfterPercent(Boolean stopAfterPercent) {
                mineConfig.stopAfterPercent = stopAfterPercent;
                return this;
            }

            public MineConfig build() {
                return this.mineConfig;
            }
        }
    }


    public static class ConfigBean {
        private final MineConfig mineConfig;

        private final LogConfig logConfig;

        public ConfigBean () {
            Optional<Properties> props = ConfigUtil.loadUserProps();
            this.logConfig = new LogConfig();
            this.mineConfig = new MineConfig();
            Properties toSave = props.orElse(new Properties());

            toSave.setProperty(MINES_PLAYMODE_KEY, mineConfig.getPlayMode().getPlayModeName());

            toSave.setProperty(MINES_GRID_WIDTH_KEY,String.valueOf(mineConfig.getGridCX()));

            toSave.setProperty(MINES_GRID_HEIGHT_KEY, String.valueOf(mineConfig.getGridCY()));

            toSave.setProperty(MINES_COUNT_KEY, String.valueOf(mineConfig.getMinesCount()));

            toSave.setProperty(MINES_LEVELUP_KEY, String.valueOf(mineConfig.getLevelUp()));

            toSave.setProperty(MINES_LEVELUP_KEY, String.valueOf(mineConfig.getStopAfterPercent()));

            toSave.setProperty(LOG_CONFIG_PATH_KEY, String.valueOf(logConfig.configPath()));

            toSave.setProperty(LOG_MIN_LEVEL_KEY, String.valueOf(logConfig.minLevel()));

            ConfigUtil.saveUserPropsIfVerChanged(toSave);
        }

        public LogConfig getLogConfig() {
            return logConfig;
        }

        public MineConfig getMineConfig() {
            return mineConfig;
        }
    }
}
