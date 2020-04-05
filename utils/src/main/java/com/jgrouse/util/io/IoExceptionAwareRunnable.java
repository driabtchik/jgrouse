package com.jgrouse.util.io;

import java.io.IOException;

@FunctionalInterface
public interface IoExceptionAwareRunnable {
    void run() throws IOException;
}
