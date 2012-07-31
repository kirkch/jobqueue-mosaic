package com.mosaic;

/**
 *
 */
public class TimeoutException extends RuntimeException {
    public TimeoutException() {
        super();
    }

    public TimeoutException( String message ) {
        super( message );
    }

    public TimeoutException( String message, Throwable cause ) {
        super( message, cause );
    }

    public TimeoutException( Throwable cause ) {
        super( cause );
    }
}
