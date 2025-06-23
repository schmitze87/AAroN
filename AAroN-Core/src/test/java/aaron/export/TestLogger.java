package aaron.export;

import aaron.logging.Logger;

public class TestLogger implements Logger {

    private org.slf4j.Logger logger;

    public TestLogger(org.slf4j.Logger logger) {
        logger = logger;
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
    public void debug(String s, Object... argument) {
        logger.debug(s, argument);
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
    public void info(String s, Object... argument) {
        logger.info(s, argument);
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
    public void warn(String s, Object... argument) {
        logger.warn(s, argument);
    }

    @Override
    public void error(String s) {
        logger.error(s);
    }

    @Override
    public void error(String s, Throwable throwable) {
        logger.error(s, throwable);
    }

    @Override
    public void error(String s, Object... argument) {
        logger.error(s, argument);
    }

}
