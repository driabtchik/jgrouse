package com.jgrouse.util;

@FunctionalInterface
public interface ExceptionAwareFunction<T, R> {
    R apply(T t) throws Exception;

    default R uncheckedApply(final T t) {
        try {
            return this.apply(t);
        } catch (Exception ex) {
            throw new UncheckedException(ex);
        }
    }
}
