package io.github.hglabplh_tech.mines.backend.logexcp;

import io.github.hglabplh_tech.mines.backend.config.Configuration;
import org.tinylog.Logger;

public class GameLogger {

    private static GameLogger logInstance;

    private GameLogger() {
        System.setProperty("tinylog.configuration", Configuration.getConfigBeanInstance().getLogConfig().configPath());

    }

    public static GameLogger logInstance() {
        if (logInstance == null) {
            logInstance = new GameLogger();
        }
        return logInstance;
    }


    public void logInfo(LoggingID logMSG, Object... params) {
        Logger.info(String.format("%s:%s", logMSG.prefix(),String.format(logMSG.message(), params)));
    }

    public void logDebug(LoggingID logMSG, Object... params) {
        Logger.debug(String.format("%s:%s", logMSG.prefix(),String.format(logMSG.message(), params)));
    }

    public void logError(LoggingID logMSG, Object... params) {
        Logger.error(String.format("%s:%s", logMSG.prefix(),String.format(logMSG.message(), params)));
    }
}
