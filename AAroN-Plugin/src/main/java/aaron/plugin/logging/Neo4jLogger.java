package aaron.plugin.logging;

import aaron.logging.Logger;
import org.neo4j.logging.Log;

public class Neo4jLogger implements Logger {

    private final Log neo4jLogger;

    public Neo4jLogger(Log neo4jLogger) {
        this.neo4jLogger = neo4jLogger;
    }

    @Override
    public void debug(String message) {
        neo4jLogger.debug(message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        neo4jLogger.debug(message, throwable);
    }

    @Override
    public void info(String message) {
        neo4jLogger.info(message);
    }

    @Override
    public void info(String message, Throwable throwable) {
        neo4jLogger.info(message, throwable);
    }

    @Override
    public void warn(String message) {
        neo4jLogger.warn(message);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        neo4jLogger.warn(message, throwable);
    }

    @Override
    public void error(String message) {
        neo4jLogger.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        neo4jLogger.error(message, throwable);
    }

}
