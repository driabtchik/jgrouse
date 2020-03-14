package com.jgrouse.util.io;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface InputStreamConsumer {
    void accept(InputStream inputStream) throws IOException;
}
