package com.jgrouse.util.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamFunction<T> {
    T apply(InputStream inputStream) throws IOException;
}
