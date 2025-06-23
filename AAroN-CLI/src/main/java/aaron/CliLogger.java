package aaron;

import aaron.logging.Logger;

public class CliLogger implements Logger {

    private final org.slf4j.Logger logger;

    public CliLogger() {
        logger = org.slf4j.LoggerFactory.getLogger(CliLogger.class);
    }

    @Override
    public void debug(String s) {
        logger.debug(s);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        logger.debug(s, throwable);
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void info(String s, Throwable throwable) {
        logger.info(s, throwable);
    }

    @Override
    public void warn(String s) {
        logger.warn(s);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        logger.warn(s, throwable);
    }

    @Override
    public void error(String s) {
        logger.error(s);
    }

    @Override
    public void error(String s, Throwable throwable) {
        logger.error(s, throwable);
    }

}
