package com.jgrouse.util.io;

import com.jgrouse.util.ExceptionAwareRunnable;
import com.jgrouse.util.ExceptionAwareSupplier;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class IoRuntimeException extends RuntimeException {

    public IoRuntimeException(final Throwable cause) {
        super(cause);
    }

    public static void unchecked(final ExceptionAwareRunnable<IOException> runnable) {
        try {
            runnable.run();
        } catch (final IOException e) {
            throw new IoRuntimeException(e);
        }
    }

    public static <T> T asUnchecked(final ExceptionAwareSupplier<T, IOException> supplier) {
        try {
            return supplier.get();
        } catch (final IOException e) {
            throw new IoRuntimeException(e);
        }
    }

}
