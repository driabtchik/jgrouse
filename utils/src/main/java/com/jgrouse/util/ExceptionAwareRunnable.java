package com.jgrouse.util;

@FunctionalInterface
public interface ExceptionAwareRunnable<T extends Exception> {
    void run() throws T;
}
