package com.jgrouse.util.io;

import com.jgrouse.util.ExceptionAwareSupplier;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamGuard {

    static <T> T withInputStream(final ExceptionAwareSupplier<InputStream, IOException> inputStreamSupplier,
                                 final InputStreamFunction<T> inputStreamFunction) {
        return IoRuntimeException.asUnchecked(() -> {
            try (InputStream is = inputStreamSupplier.get()) {
                return inputStreamFunction.apply(is);
            }
        });
    }

    static void withInputStream(final ExceptionAwareSupplier<InputStream, IOException> inputStreamSupplier,
                                final InputStreamConsumer inputStreamConsumer) {
        withInputStream(inputStreamSupplier, is -> {
            inputStreamConsumer.accept(is);
            return null;
        });
    }
}
