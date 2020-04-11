package com.jgrouse.util.io;

import com.jgrouse.util.ExceptionAwareRunnable;
import com.jgrouse.util.ExceptionAwareSupplier;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.jgrouse.util.io.IoRuntimeException.asUnchecked;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class IoRuntimeExceptionTest {

    private static final String THIS_IS_ERROR = "this is error";

    @Test
    public void testAsUnchecked_withSupplier_noErrors() {
        String strValue = "foo";
        assertThat(IoRuntimeException.asUnchecked(() -> strValue)).isSameAs(strValue);
    }

    @Test
    public void testAsUnchecked_withSupplier_withException() {
        ExceptionAwareSupplier<String, IOException> supplierWithException = () -> {
            throw new IOException(THIS_IS_ERROR);
        };

        assertThatThrownBy(() -> asUnchecked(supplierWithException))
          .isInstanceOf(IoRuntimeException.class)
          .hasMessageContaining(THIS_IS_ERROR);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAsUnchecked_withRunnable_noError() throws IOException {
        ExceptionAwareRunnable<IOException> runnable = mock(ExceptionAwareRunnable.class);
        IoRuntimeException.asUnchecked(runnable);
        verify(runnable).run();
    }

    @Test
    public void testAsUnchecked_withRunnable_withException() {
        ExceptionAwareRunnable<IOException> runnable = () -> {
            throw new IOException(THIS_IS_ERROR);
        };
        assertThatThrownBy(() -> IoRuntimeException.asUnchecked(runnable))
          .isInstanceOf(IoRuntimeException.class)
          .hasMessageContaining(THIS_IS_ERROR);
    }
}
