/*
 * Copyright (C) 2025 Harald Glab-Plhak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
