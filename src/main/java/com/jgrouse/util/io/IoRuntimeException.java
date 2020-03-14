package com.jgrouse.util.io;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class IoRuntimeException extends RuntimeException {
    public IoRuntimeException() {
    }

    public IoRuntimeException(String message) {
        super(message);
    }

    public IoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IoRuntimeException(Throwable cause) {
        super(cause);
    }

    public IoRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void asUnchecked(IoExceptionAwareRunnable runnable) {
        try {
            runnable.run();
        } catch (IOException e) {
            throw new IoRuntimeException(e);
        }
    }

    public static <T> T asUnchecked(IoExceptionAwareSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (IOException e) {
            throw new IoRuntimeException(e);
        }
    }

}
