package com.jgrouse.util.io;


import com.jgrouse.util.ExceptionAwareSupplier;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.InputStream;

import static com.jgrouse.util.io.InputStreamGuard.withInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class InputStreamGuardTest {

    private static final String THIS_IS_ERROR_MESSAGE = "this is error message";
    private static final String RESULT = "foo";
    private final InputStream inputStream = mock(InputStream.class);

    @SuppressWarnings("unchecked")
    private final ExceptionAwareSupplier<InputStream, IOException> inputStreamSupplier =
      mock(ExceptionAwareSupplier.class);

    private final InputStreamConsumer inputStreamConsumer = mock(InputStreamConsumer.class);

    @SuppressWarnings("unchecked")
    private final InputStreamFunction<String> inputStreamFunction = mock(InputStreamFunction.class);

    private final InOrder order = inOrder(inputStream, inputStreamSupplier, inputStreamFunction, inputStreamConsumer);

    @Test
    public void testWithInputStream_errorInStreamCreation() throws IOException {
        when(inputStreamSupplier.get()).thenThrow(new IOException(THIS_IS_ERROR_MESSAGE));
        assertThatThrownBy(() -> withInputStream(inputStreamSupplier, inputStreamFunction))
          .isInstanceOf(IoRuntimeException.class)
          .hasMessageContaining(THIS_IS_ERROR_MESSAGE);
        order.verify(inputStreamSupplier).get();
        order.verifyNoMoreInteractions();
    }

    @Test
    public void testWithInputStream_normal() throws IOException {
        when(inputStreamSupplier.get()).thenReturn(inputStream);
        when(inputStreamFunction.apply(inputStream)).thenReturn(RESULT);
        final String res = withInputStream(inputStreamSupplier, inputStreamFunction);
        assertThat(res).isSameAs(RESULT);
        order.verify(inputStreamSupplier).get();
        order.verify(inputStreamFunction).apply(inputStream);
        order.verify(inputStream).close();
    }

    @Test
    public void testWithInputStream_errorInConsumer() throws IOException {
        when(inputStreamSupplier.get()).thenReturn(inputStream);
        when(inputStreamFunction.apply(inputStream)).thenThrow(new IOException(THIS_IS_ERROR_MESSAGE));
        assertThatThrownBy(() -> withInputStream(inputStreamSupplier, inputStreamFunction))
          .isInstanceOf(IoRuntimeException.class)
          .hasMessageContaining(THIS_IS_ERROR_MESSAGE);
        verify(inputStream).close();
    }

    @Test
    public void testWithInputStream_withConsumer() throws IOException {
        when(inputStreamSupplier.get()).thenReturn(inputStream);
        withInputStream(inputStreamSupplier, inputStreamConsumer);
        order.verify(inputStreamSupplier).get();
        order.verify(inputStreamConsumer).accept(inputStream);
        order.verify(inputStream).close();

    }
}
