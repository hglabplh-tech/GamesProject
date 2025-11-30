/*
 * Copyright (c)
 */

package io.github.hglabplh_tech.mines.backend.config;

public class Configuration {
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
