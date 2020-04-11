package com.jgrouse.util.io;

import com.jgrouse.util.ExceptionAwareRunnable;
import com.jgrouse.util.ExceptionAwareSupplier;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class IoRuntimeException extends RuntimeException {

    public IoRuntimeException(Throwable cause) {
        super(cause);
    }

    public static void asUnchecked(ExceptionAwareRunnable<IOException> runnable) {
        try {
            runnable.run();
        } catch (IOException e) {
            throw new IoRuntimeException(e);
        }
    }

    public static <T> T asUnchecked(ExceptionAwareSupplier<T, IOException> supplier) {
        try {
            return supplier.get();
        } catch (IOException e) {
            throw new IoRuntimeException(e);
        }
    }

}
