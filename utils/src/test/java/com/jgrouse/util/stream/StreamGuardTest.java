package com.jgrouse.util.stream;

import com.jgrouse.util.ExceptionAwareSupplier;
import com.jgrouse.util.UncheckedException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jgrouse.util.stream.StreamGuard.guardResource;
import static com.jgrouse.util.stream.StreamGuard.guardResourceSupplier;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StreamGuardTest {

    @Test
    void usageExample_autoClosingSingleResource() {
        final String content = "foo\nbar";

        final StreamGuard<String> guard = guardResource(() -> new ByteArrayInputStream(content.getBytes(UTF_8)),
                inputStream -> {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
                    return reader.lines();
                });

        final StreamGuard<String> transformedGuard = guard.transform(s -> s.map(x -> x + "xxx"));

        assertConsumedContent(transformedGuard);
    }

    private void assertConsumedContent(final StreamGuard<String> guard) {
        final List<String> result = guard.consume(s -> s.collect(Collectors.toList()));
        assertThat(result).containsExactly("fooxxx", "barxxx");
    }

    @Test
    void usageExample_autoClosingSpecificSupplier() {
        final StreamGuard<String> guard = guardResourceSupplier(new StagedResourceSupplier("foo\nbar"),
                inputStream -> {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
                    return reader.lines();
                }).transform(s -> s.map(x -> x + "xxx"));

        assertConsumedContent(guard);
    }

    @Test
    void guard_whenSupplierThrowsException() {
        assertThatExceptionOfType(UncheckedException.class)
                .isThrownBy(() ->
                        guardResource(() -> {
                                    throw new IllegalArgumentException("breaking exception");
                                }, s -> Stream.of("foobar")
                        ).transform(s -> s.map(x -> x + "xxx"))
                                .consume(Stream::count)
                )
                .satisfies(ex -> assertThat(ex.getCause()).hasMessageContaining("breaking exception"));
    }

    static class StagedResourceSupplier implements ExceptionAwareSupplier<InputStream, Exception>, AutoCloseable {

        private final String content;

        private InputStream stream;

        StagedResourceSupplier(@SuppressWarnings("SameParameterValue") final String content) {
            this.content = content;
        }

        @Override
        public void close() throws Exception {
            if (stream != null) {
                stream.close();
            }
        }

        @Override
        public InputStream get() {
            stream = new ByteArrayInputStream(content.getBytes(UTF_8));
            return stream;
        }
    }

    @Test
    void closingOfResourceThrowsException() throws Exception {
        final AutoCloseable closeable = mock(AutoCloseable.class);
        doAnswer(x -> {
            throw new IllegalArgumentException("forcing error");
        }).when(closeable).close();
        assertThatExceptionOfType(StreamGuardException.class).isThrownBy(() ->
                guardResource(() -> closeable, c -> Stream.of("foo", "bar")).consume(Stream::count)
        ).satisfies(ex -> assertThat(ex.getMessage()).contains("forcing error"));
        verify(closeable).close();
    }


    @Test
    void constructionOfGuardShouldNotCreateResourceIfNotConsumed() {
        assertThatCode(() -> guardResource(() -> {
            throw new IllegalArgumentException("in resource construction");
        }, s -> Stream.of("foo"))).doesNotThrowAnyException();
    }

    @Test
    void constructionOfGuardShouldNotTriggerProductionOfResourceStreamIfNotConsumed() {
        @SuppressWarnings("unchecked") final ExceptionAwareSupplier<AutoCloseable, Exception> resourceSupplier = mock(ExceptionAwareSupplier.class);

        assertThatCode(() -> guardResource(resourceSupplier, r -> {
            throw new IllegalArgumentException("in stream construction");
        })).doesNotThrowAnyException();
        verifyNoInteractions(resourceSupplier);
    }

    @Test
    void transformationOfGuardShouldNotTriggerProductionOfResourceStreamIfNotConsumed() {
        @SuppressWarnings("unchecked") final ExceptionAwareSupplier<AutoCloseable, Exception> resourceSupplier = mock(ExceptionAwareSupplier.class);

        assertThatExceptionOfType(UncheckedException.class).isThrownBy(() ->
                guardResource(resourceSupplier, r -> Stream.of("foo")).transform(s -> {
                    throw new IllegalArgumentException("in transformer");
                })).satisfies(ex -> assertThat(ex.getCause()).hasMessageContaining("in transformer"));
        verifyNoInteractions(resourceSupplier);
    }


    @Test
    void resourceIsClosedWhenExceptionIsThrownDuringConsumptionWhenCreatingStreamFromResource() throws Exception {
        final AutoCloseable resource = mock(AutoCloseable.class);
        assertThatExceptionOfType(UncheckedException.class)
                .isThrownBy(() -> guardResource(() -> resource, r -> {
                    throw new IllegalStateException("breaking processing");
                }).consume(Stream::count))
                .satisfies(ex -> assertThat(ex.getCause()).hasMessageContaining("breaking processing"));
        verify(resource).close();
    }

    @Test
    void resourceIsClosedEvenIfTransformationProducesNonDerivedStream() throws Exception {
        final AutoCloseable resource = mock(AutoCloseable.class);

        guardResource(() -> resource, r -> Stream.of("foo")).transform(s -> Stream.of("bar " + s.count())).consume(Stream::count);

        verify(resource).close();
    }

    @Test
    void resourceIsClosedWhenExceptionIsThrownDuringTransformation() throws Exception {
        final AutoCloseable resource = mock(AutoCloseable.class);

        assertThatExceptionOfType(UncheckedException.class).isThrownBy(() ->
                guardResource(() -> resource, r -> Stream.of("foo")).transform(s -> s.map(r -> {
                    throw new IllegalArgumentException("in transform");
                })).consume(Stream::count))
                .satisfies(ex -> assertThat(ex.getCause()).hasMessageContaining("in transform"));

        verify(resource).close();

    }


}