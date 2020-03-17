package com.jgrouse.util.stream;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jgrouse.util.stream.StreamGuard.guard;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StreamGuardTest {

    @Test
    void usageExample() {
        String content = "foo\nbar";

        StreamGuard<String> guard = guard(() -> new ByteArrayInputStream(content.getBytes()),
                inputStream -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    return reader.lines();
                }).transform(s -> s.map(x -> x + "xxx"));

        List<String> result = guard.consume(s -> s.collect(Collectors.toList()));
        assertThat(result).containsExactly("fooxxx", "barxxx");
    }


    @Test
    void constructionOfGuardShouldNotCreateResourceIfNotConsumed() {
        assertThatCode(() -> guard(() -> {
            throw new IllegalArgumentException("in resource construction");
        }, s -> Stream.of("foo"))).doesNotThrowAnyException();
    }

    @Test
    void constructionOfGuardShouldNotTriggerProductionOfResourceStreamIfNotConsumed() {
        @SuppressWarnings("unchecked")
        Supplier<AutoCloseable> resourceSupplier = mock(Supplier.class);

        assertThatCode(() -> guard(resourceSupplier, r -> {
            throw new IllegalArgumentException("in stream construction");
        })).doesNotThrowAnyException();
        verifyNoInteractions(resourceSupplier);
    }

    @Test
    void transformationOfGuardShouldNotTriggerProductionOfResourceStreamIfNotConsumed() {
        @SuppressWarnings("unchecked")
        Supplier<AutoCloseable> resourceSupplier = mock(Supplier.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                guard(resourceSupplier, r -> Stream.of("foo")).transform(s -> {
                    throw new IllegalArgumentException("in transformer");
                }));
        verifyNoInteractions(resourceSupplier);
    }


    @Test
    void resourceIsClosedWhenExceptionIsThrownDuringConsumptionWhenCreatingStreamFromResource() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);
        assertThatThrownBy(() -> guard(() -> resource, r -> {
            throw new IllegalStateException("breaking processing");
        }).consume(Stream::count)).isInstanceOf(IllegalStateException.class);
        verify(resource).close();
    }

    @Test
    void resourceIsClosedEvenIfTransformationProducesNonDerivedStream() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);

        guard(() -> resource, r -> Stream.of("foo")).transform(s -> Stream.of("bar " + s.count())).consume(Stream::count);

        verify(resource).close();
    }

    @Test
    void resourceIsClosedWhenExceptionIsThrownDuringTransformation() throws Exception {
        AutoCloseable resource = mock(AutoCloseable.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                guard(() -> resource, r -> Stream.of("foo")).transform(s -> s.map(r -> {
                    throw new IllegalArgumentException("in transform");
                })).consume(Stream::count));

        verify(resource).close();

    }


}