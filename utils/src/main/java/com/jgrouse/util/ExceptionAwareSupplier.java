package com.jgrouse.util;

@FunctionalInterface
public interface ExceptionAwareSupplier<T, E extends Exception> {
    T get() throws E;

    default T uncheckedGet() {
        try {
            return get();
        } catch (Exception e) {
            throw new UncheckedException(e);
        }
    }
}
