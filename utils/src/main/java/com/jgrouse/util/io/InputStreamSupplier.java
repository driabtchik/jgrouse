package com.jgrouse.util.io;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface InputStreamSupplier {
  @NotNull
  InputStream get() throws IOException;
}
