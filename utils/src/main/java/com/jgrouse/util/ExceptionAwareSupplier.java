package com.jgrouse.util;

@FunctionalInterface
public interface ExceptionAwareSupplier<T, E extends Exception> {
    T get() throws E;
}
