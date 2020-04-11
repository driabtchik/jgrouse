package com.jgrouse.util.jdbc;

import com.jgrouse.util.ExceptionAwareRunnable;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

class JdbcRuntimeExceptionTest {

    @Test
    void asUnchecked_runnable_noException() {
        boolean[] invoked = {false};
        assertThatCode(() -> JdbcRuntimeException.asUnchecked(() -> {
            invoked[0] = true;
        })).doesNotThrowAnyException();
        assertThat(invoked[0]).isTrue();
    }

    @Test
    void asUnchecked_runnable_withException() {
        boolean[] invoked = {false};
        SQLException sqlException = new SQLException("foobar");

        ExceptionAwareRunnable<SQLException> exceptionAwareRunnable = () -> {
            invoked[0] = true;
            throw sqlException;
        };

        assertThatExceptionOfType(JdbcRuntimeException.class)
                .isThrownBy(() -> JdbcRuntimeException.asUnchecked(exceptionAwareRunnable))
                .satisfies(ex -> assertThat(ex.getCause()).isSameAs(sqlException));
        assertThat(invoked[0]).isTrue();
    }

    @Test
    void asUnchecked_supplier_noException() {
        assertThat(JdbcRuntimeException.asUnchecked(() -> 42)).isEqualTo(42);
    }

    @Test
    void asUnchecked_supplier_withException() {
        SQLException sqlException = new SQLException("foobar");
        assertThatExceptionOfType(JdbcRuntimeException.class)
                .isThrownBy(() -> JdbcRuntimeException.asUnchecked(() -> exceptionThrower(sqlException)))
                .satisfies(ex -> assertThat(ex.getCause()).isSameAs(sqlException));
    }

    private String exceptionThrower(SQLException exception) throws SQLException {
        throw exception;
    }

}