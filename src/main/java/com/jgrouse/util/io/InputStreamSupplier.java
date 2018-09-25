package com.jgrouse.util.io;

import java.io.IOException;
import java.io.InputStream;

import javax.validation.constraints.NotNull;

@FunctionalInterface
public interface InputStreamSupplier {
  @SuppressWarnings("RedundantThrows")
  @NotNull
  InputStream get() throws IOException;
}
