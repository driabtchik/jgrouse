package com.jgrouse.util.jdbc;

import com.jgrouse.util.ExceptionAwareRunnable;
import com.jgrouse.util.ExceptionAwareSupplier;

import java.sql.SQLException;

public class JdbcRuntimeException extends RuntimeException {

    public JdbcRuntimeException(String message) {
        super(message);
    }

    public JdbcRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcRuntimeException(Throwable cause) {
        super(cause);
    }

    public JdbcRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void asUnchecked(ExceptionAwareRunnable<SQLException> runnable) {
        try {
            runnable.run();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    public static <T> T asUnchecked(ExceptionAwareSupplier<T, SQLException> supplier) {
        try {
            return supplier.get();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

}
