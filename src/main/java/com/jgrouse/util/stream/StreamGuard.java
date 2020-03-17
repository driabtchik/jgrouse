package com.jgrouse.util.stream;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.jgrouse.util.Assert.isNull;

public class StreamGuard<T> {

    private final Stream<T> stream;

    private StreamGuard(Stream<T> stream) {
        this.stream = stream;
    }

    public static <R extends AutoCloseable, T> StreamGuard<T> guard(Supplier<R> resourceSupplier,
                                                                    Function<R, Stream<T>> streamGenerator) {
        ResourceGuard<R> guard = new ResourceGuard<>(resourceSupplier);

        Stream<T> stream = Stream.of(guard)
                .onClose(guard::close)
                .map(ResourceGuard::get)
                .flatMap(streamGenerator);
        return new StreamGuard<>(stream);
    }

    public <C> StreamGuard<C> transform(Function<Stream<T>, Stream<C>> transformer) {
        return new StreamGuard<>(transformer.apply(this.stream).onClose(this.stream::close));
    }


    public <R> R consume(Function<Stream<T>, R> consumer) {
        try {
            return consumer.apply(stream);
        } finally {
            stream.close();
        }
    }

    private static class ResourceGuard<R extends AutoCloseable> implements Supplier<R> {
        private final Supplier<R> originalSuppler;
        private R resource;

        protected ResourceGuard(Supplier<R> originalSuppler) {
            this.originalSuppler = originalSuppler;
        }

        @Override
        public R get() {
            isNull(resource, "Resource supplier had been already invoked");
            resource = originalSuppler.get();
            return resource;
        }

        void close() {
            try {
                if (resource != null) {
                    resource.close();
                    resource = null;
                }
            } catch (Exception e) {
                throw new StreamGuardException(e);
            }
        }
    }
}
