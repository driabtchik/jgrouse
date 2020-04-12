package com.jgrouse.util.stream;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.jgrouse.util.Assert.isNull;

public class StreamGuard<T> {

    private final Stream<T> stream;

    private StreamGuard(final Stream<T> stream) {
        this.stream = stream;
    }

    public static <R extends AutoCloseable, T> StreamGuard<T> guardResource(final Supplier<R> resourceSupplier,
                                                                            final Function<R, Stream<T>> streamGenerator) {
        final ResourceGuard<R> guard = new ResourceGuard<>(resourceSupplier);

        final Stream<T> stream = Stream.of(guard)
                .onClose(guard::close)
                .map(ResourceGuard::get)
                .flatMap(streamGenerator);
        return new StreamGuard<>(stream);
    }

    public static <R, S extends AutoCloseable & Supplier<R>, T> StreamGuard<T> guardResourceSupplier(final S resourceSupplier,
                                                                                                     final Function<R, Stream<T>> streamGenerator) {
        @SuppressWarnings("Convert2MethodRef") final Stream<T> stream = Stream.of(resourceSupplier)
                .onClose(() -> closeWithRethrow(resourceSupplier))
                .map(r -> r.get()) //Do not convert r.get to method reference due to a bug in JDK 1.8 compiler
                .flatMap(streamGenerator);
        return new StreamGuard<>(stream);

    }

    public <C> StreamGuard<C> transform(final Function<Stream<T>, Stream<C>> transformer) {
        return new StreamGuard<>(transformer.apply(this.stream).onClose(this.stream::close));
    }


    public <R> R consume(final Function<Stream<T>, R> consumer) {
        try {
            return consumer.apply(stream);
        } finally {
            stream.close();
        }
    }

    private static void closeWithRethrow(final AutoCloseable autoCloseable) {
        try {
            autoCloseable.close();
        } catch (final Exception ex) {
            throw new StreamGuardException(ex);
        }
    }

    private static class ResourceGuard<R extends AutoCloseable> implements Supplier<R>, AutoCloseable {
        private final Supplier<R> originalSuppler;
        private R resource;

        protected ResourceGuard(final Supplier<R> originalSuppler) {
            this.originalSuppler = originalSuppler;
        }

        @Override
        public R get() {
            isNull(resource, "Resource supplier had been already invoked");
            resource = originalSuppler.get();
            return resource;
        }

        @Override
        public void close() {
            if (resource != null) {
                closeWithRethrow(resource);
                resource = null;
            }
        }
    }
}
