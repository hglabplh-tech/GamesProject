package io.github.hglabplh_tech.games.backend.logexcp;

public enum LoggingID {

    MINELOG_DEB_ID_00001(LogLevel.DEBUG, "MINES", "Count Mines: %d"),
    MINELOG_DEB_ID_00002(LogLevel.DEBUG, "MINES", "PlayGround (CX,CY): (%d, %d)"),
    MINELOG_DEB_ID_00003(LogLevel.DEBUG, "MINES", "PlayMode: %s"),
    MINELOG_DEB_ID_00004(LogLevel.DEBUG, "MINES", "LabyrinthPoints: %s %s %s %s"),
    MINELOG_DEB_ID_00005(LogLevel.DEBUG, "MINES", "==== Print out path trials ===="),
    MINELOG_DEB_ID_00006(LogLevel.DEBUG, "MINES", "======= Print out path  ======="),
    MINELOG_DEB_ID_00007(LogLevel.DEBUG, "MINES", "Entry --> %s"),
    MINELOG_DEB_ID_00008(LogLevel.DEBUG, "MINES", "Path calculated is correct"),
    MINELOG_DEB_ID_00009(LogLevel.DEBUG, "MINES", "Path calculated is NOT correct"),
    MINELOG_DEB_ID_00010(LogLevel.DEBUG, "MINES", "Path calculated is NOT correct in points (%s --/-- %s)"),
    MINELOG_DEB_ID_00011(LogLevel.DEBUG, "MINES", "Path size is %d  points found count is %d"),

    MINELOG_TRC_ID_00501(LogLevel.TRACE, "MINES", "Path trial at: %s // calculated from %s with %s"),
    MINELOG_TRC_ID_00502(LogLevel.TRACE, "MINES", "Path trial %s stored in path chain -> success is got in %d trials"),
    MINELOG_TRC_ID_00503(LogLevel.TRACE, "MINES", "Path trial at: %s // calculated from %s"),

    MINELOG_TRC_ID_00511(LogLevel.TRACE, "MINES", "Path broken at %s"),

    MINELOG_INF_ID_01001(LogLevel.INFO, "MINES", "Initialize Playground start"),
    MINELOG_INF_ID_01002(LogLevel.INFO, "MINES", "Initialize Playground restart"),
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
        ERROR,
        TRACE,
        WARN
    }
}
