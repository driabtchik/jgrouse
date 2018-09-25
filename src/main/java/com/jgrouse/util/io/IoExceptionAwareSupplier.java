package com.jgrouse.util.io;

import java.io.IOException;

@FunctionalInterface
public interface IoExceptionAwareSupplier<T> {
  T get() throws IOException;
}
