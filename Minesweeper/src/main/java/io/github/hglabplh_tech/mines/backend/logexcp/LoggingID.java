package io.github.hglabplh_tech.mines.backend.logexcp;

public enum LoggingID {

    MINELOG_ID_00001(LogLevel.DEBUG, "MINES", "Count Mines: %d"),
    MINELOG_ID_00002(LogLevel.DEBUG, "MINES", "PlayGround (CX,CY): (%d, %d)"),
    MINELOG_ID_00003(LogLevel.DEBUG, "MINES", "PlayMode: %s"),

    ;

    private final LogLevel logLevel;

    private final String prefix;

    private final String message;
    LoggingID(LogLevel logLevel, String prefix, String message) {
        this.logLevel = logLevel;
        this.prefix = prefix;
        this.message = message;
    }

    public String prefix() {
        return this.prefix;
    }

    public String message() {
        return this.message;
    }

    public String formattedMessage(Object... params) {
        String fullMessage = new StringBuilder().append(this.prefix()).append(" : ").append(this.message()).toString();
        return String.format(fullMessage, params);
    }

    public LogLevel logLevel() {
        return this.logLevel;
    }

    enum LogLevel {
        INFO,
        DEBUG,
        ERROR
    }
}
