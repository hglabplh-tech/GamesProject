package io.github.hglabplh_tech.games.backend.logexcp;


import io.github.hglabplh_tech.games.backend.config.Configuration;
import org.tinylog.Logger;

public class GameLogger {

    private static GameLogger logInstance;

    private GameLogger() {
        System.setProperty("tinylog.configuration", Configuration.getConfigBeanInstance().logConfig().configPath());
        System.setProperty("tinylog.directory", Configuration.getConfigBeanInstance().logConfig().loggingPath());
    }

    public static GameLogger logInstance() {
        if (logInstance == null) {
            logInstance = new GameLogger();
        }
        return logInstance;
    }


    public void logInfo(LoggingID logMSG, Object... params) {
        Logger.info(logMSG.formattedMessage(params));
    }

    public void logDebug(LoggingID logMSG, Object... params) {
        Logger.debug(logMSG.formattedMessage(params));
    }

    public void logError(LoggingID logMSG, Object... params) {
        Logger.error(logMSG.formattedMessage(params));
    }
    public void logTrace(LoggingID logMSG, Object... params) {
        Logger.trace(logMSG.formattedMessage(params));
    }

    public void logWarn(LoggingID logMSG, Object... params) {
        Logger.warn(logMSG.formattedMessage(params));
    }

}
