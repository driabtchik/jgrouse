package com.jgrouse.util.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamConsumer {
    void accept(InputStream inputStream) throws IOException;
}
