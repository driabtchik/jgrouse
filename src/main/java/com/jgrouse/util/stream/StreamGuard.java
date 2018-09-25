package com.jgrouse.util.stream;

import static com.jgrouse.util.Assert.isNull;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StreamGuard<T> {

  private Stream<T> stream;

  StreamGuard(Stream<T> stream) {
    this.stream = stream;
  }

  public <R extends AutoCloseable> StreamGuard(Supplier<R> resourceSupplier,
                                               Function<Supplier<R>, Stream<T>> streamGenerator) {
    ResourceGuard<R> guard = new ResourceGuard<>(resourceSupplier);
    boolean ok = false;
    try {
      Stream<T> localStream = streamGenerator.apply(guard);
      this.stream = localStream.onClose(guard::close);
      ok = true;
    } finally {
      if (!ok) {
        guard.close();
      }
    }
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
        }
      } catch (Exception e) {
        throw new StreamGuardException(e);
      }
    }
  }
}
