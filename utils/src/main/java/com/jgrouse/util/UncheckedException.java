package com.jgrouse.util;

public class UncheckedException extends RuntimeException {
    public UncheckedException(final Throwable cause) {
        super(cause);
    }
}
