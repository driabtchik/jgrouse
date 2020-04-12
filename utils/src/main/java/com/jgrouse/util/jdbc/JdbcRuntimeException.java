package com.jgrouse.util.jdbc;

import com.jgrouse.util.ExceptionAwareRunnable;
import com.jgrouse.util.ExceptionAwareSupplier;

import java.sql.SQLException;

public class JdbcRuntimeException extends RuntimeException {

    public JdbcRuntimeException(final Throwable cause) {
        super(cause);
    }

    public static void unchecked(final ExceptionAwareRunnable<SQLException> runnable) {
        try {
            runnable.run();
        } catch (final SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    public static <T> T asUnchecked(final ExceptionAwareSupplier<T, SQLException> supplier) {
        try {
            return supplier.get();
        } catch (final SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

}
