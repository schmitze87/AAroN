package aaron.logging;

public interface Logger {

    /**
     * @param message The message to be written
     */
    void debug( String message );

    /**
     * @param message   The message to be written
     * @param throwable An exception that will also be written
     */
    void debug( String message, Throwable throwable );

    /**
     * @param format    A string format for writing a message
     * @param arguments Arguments to substitute into the message according to the format
     */
    void debug( String format, Object... arguments );

    /**
     * @param message The message to be written
     */
    void info( String message );

    /**
     * @param message   The message to be written
     * @param throwable An exception that will also be written
     */
    void info( String message, Throwable throwable );

    /**
     * @param format    A string format for writing a message
     * @param arguments Arguments to substitute into the message according to the format
     */
    void info( String format, Object... arguments );

    /**
     * @param message The message to be written
     */
    void warn( String message );

    /**
     * @param message   The message to be written
     * @param throwable An exception that will also be written
     */
    void warn( String message, Throwable throwable );

    /**
     * @param format    A string format for writing a message
     * @param arguments Arguments to substitute into the message according to the format
     */
    void warn( String format, Object... arguments );

    /**
     * @param message The message to be written
     */
    void error( String message );

    /**
     * @param message   The message to be written
     * @param throwable An exception that will also be written
     */
    void error( String message, Throwable throwable );

    /**
     * @param format    A string format for writing a message
     * @param arguments Arguments to substitute into the message according to the {@code format}
     */
    void error( String format, Object... arguments );
}
