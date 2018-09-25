package com.jgrouse.util.io;

import java.io.InputStream;

public interface InputStreamGuard {

  static <T> T withInputStream(IoExceptionAwareSupplier<InputStream> inputStreamSupplier,
                               InputStreamFunction<T> inputStreamFunction) {
    return IoRuntimeException.asUnchecked(() -> {
      try (InputStream is = inputStreamSupplier.get()) {
        return inputStreamFunction.apply(is);
      }
    });
  }

  static void withInputStream(IoExceptionAwareSupplier<InputStream> inputStreamSupplier,
                              InputStreamConsumer inputStreamConsumer) {
    withInputStream(inputStreamSupplier, is -> {
      inputStreamConsumer.accept(is);
      return null;
    });
  }
}
