package com.jgrouse.util.stream;

import com.jgrouse.util.ExceptionAwareFunction;
import com.jgrouse.util.ExceptionAwareSupplier;

import java.util.stream.Stream;

import static com.jgrouse.util.Assert.isNull;

public final class StreamGuard<T> {

    private final Stream<T> stream;

    private StreamGuard(final Stream<T> stream) {
        this.stream = stream;
    }

    public static <R extends AutoCloseable, T> StreamGuard<T> guardResource(final ExceptionAwareSupplier<R, Exception> resourceSupplier,
                                                                            final ExceptionAwareFunction<R, Stream<T>> streamGenerator) {
        final ResourceGuard<R> guard = new ResourceGuard<>(resourceSupplier);

        final Stream<T> stream = Stream.of(guard)
                .onClose(guard::close)
                .map(ResourceGuard::uncheckedGet)
                .flatMap(streamGenerator::uncheckedApply);
        return new StreamGuard<>(stream);
    }

    public static <R, S extends AutoCloseable & ExceptionAwareSupplier<R, Exception>, T> StreamGuard<T> guardResourceSupplier(final S resourceSupplier,
                                                                                                                              final ExceptionAwareFunction<R, Stream<T>> streamGenerator) {
        @SuppressWarnings("Convert2MethodRef") final Stream<T> stream = Stream.of(resourceSupplier)
                .onClose(() -> closeWithRethrow(resourceSupplier))
                .map(r -> r.uncheckedGet()) //Do not convert r.get to method reference due to a bug in JDK 1.8 compiler
                .flatMap(streamGenerator::uncheckedApply);
        return new StreamGuard<>(stream);

    }

    public <C> StreamGuard<C> transform(final ExceptionAwareFunction<Stream<T>, Stream<C>> transformer) {
        return new StreamGuard<>(transformer.uncheckedApply(this.stream).onClose(this.stream::close));
    }


    public <R> R consume(final ExceptionAwareFunction<Stream<T>, R> consumer) {
        try {
            return consumer.uncheckedApply(stream);
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

    private static class ResourceGuard<R extends AutoCloseable> implements ExceptionAwareSupplier<R, Exception>, AutoCloseable {
        private final ExceptionAwareSupplier<R, Exception> originalSuppler;
        private R resource;

        protected ResourceGuard(final ExceptionAwareSupplier<R, Exception> originalSuppler) {
            this.originalSuppler = originalSuppler;
        }

        @Override
        public R get() throws Exception {
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
