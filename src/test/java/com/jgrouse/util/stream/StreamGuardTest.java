package com.jgrouse.util.stream;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jgrouse.util.stream.StreamGuard.guardResource;
import static com.jgrouse.util.stream.StreamGuard.guardResourceSupplier;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StreamGuardTest {

    @Test
    void usageExample_autoClosingSingleResource() {
        String content = "foo\nbar";

        StreamGuard<String> guard = guardResource(() -> new ByteArrayInputStream(content.getBytes()),
                inputStream -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    return reader.lines();
                });

        StreamGuard<String> transformedGuard = guard.transform(s -> s.map(x -> x + "xxx"));

        assertConsumedContent(transformedGuard);
    }

    private void assertConsumedContent(StreamGuard<String> guard) {
        List<String> result = guard.consume(s -> s.collect(Collectors.toList()));
        assertThat(result).containsExactly("fooxxx", "barxxx");
    }

    @Test
    void usageExample_autoClosingSpecificSupplier() {
        StreamGuard<String> guard = guardResourceSupplier(new StagedResourceSupplier("foo\nbar"),
                inputStream -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    return reader.lines();
                }).transform(s -> s.map(x -> x + "xxx"));

        assertConsumedContent(guard);
    }

    static class StagedResourceSupplier implements Supplier<InputStream>, AutoCloseable {

        private final String content;

        private InputStream stream;

        StagedResourceSupplier(String content) {
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
            return new ByteArrayInputStream(content.getBytes());
        }
    }


    @Test
    void constructionOfGuardShouldNotCreateResourceIfNotConsumed() {
        assertThatCode(() -> guardResource(() -> {
            throw new IllegalArgumentException("in resource construction");
        }, s -> Stream.of("foo"))).doesNotThrowAnyException();
    }

    @Test
    void constructionOfGuardShouldNotTriggerProductionOfResourceStreamIfNotConsumed() {
        @SuppressWarnings("unchecked")
        Supplier<AutoCloseable> resourceSupplier = mock(Supplier.class);

        assertThatCode(() -> guardResource(resourceSupplier, r -> {
            throw new IllegalArgumentException("in stream construction");
        })).doesNotThrowAnyException();
        verifyNoInteractions(resourceSupplier);
    }

    @Test
    void transformationOfGuardShouldNotTriggerProductionOfResourceStreamIfNotConsumed() {
        @SuppressWarnings("unchecked")
        Supplier<AutoCloseable> resourceSupplier = mock(Supplier.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                guardResource(resourceSupplier, r -> Stream.of("foo")).transform(s -> {
                    throw new IllegalArgumentException("in transformer");
                }));
        verifyNoInteractions(resourceSupplier);
    }


    @Test
    void resourceIsClosedWhenExceptionIsThrownDuringConsumptionWhenCreatingStreamFromResource() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);
        assertThatThrownBy(() -> guardResource(() -> resource, r -> {
            throw new IllegalStateException("breaking processing");
        }).consume(Stream::count)).isInstanceOf(IllegalStateException.class);
        verify(resource).close();
    }

    @Test
    void resourceIsClosedEvenIfTransformationProducesNonDerivedStream() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);

        guardResource(() -> resource, r -> Stream.of("foo")).transform(s -> Stream.of("bar " + s.count())).consume(Stream::count);

        verify(resource).close();
    }

    @Test
    void resourceIsClosedWhenExceptionIsThrownDuringTransformation() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                guardResource(() -> resource, r -> Stream.of("foo")).transform(s -> s.map(r -> {
                    throw new IllegalArgumentException("in transform");
                })).consume(Stream::count));

        verify(resource).close();

    }


}