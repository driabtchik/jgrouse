package com.jgrouse.util.io;

import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class IoRuntimeException extends RuntimeException {


    public IoRuntimeException(Throwable cause) {
        super(cause);
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
