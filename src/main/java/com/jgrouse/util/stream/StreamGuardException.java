package com.jgrouse.util.stream;

public class StreamGuardException extends RuntimeException {
    public StreamGuardException() {
    }

    public StreamGuardException(String message) {
        super(message);
    }

    public StreamGuardException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamGuardException(Throwable cause) {
        super(cause);
    }

    public StreamGuardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
