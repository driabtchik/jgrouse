package com.jgrouse.util.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamFunction<T> {
  @SuppressWarnings("RedundantThrows")
  T apply(InputStream inputStream) throws IOException;
}
