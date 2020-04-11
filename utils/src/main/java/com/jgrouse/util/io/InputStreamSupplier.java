package com.jgrouse.util.io;

import com.jgrouse.util.ExceptionAwareSupplier;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSupplier extends ExceptionAwareSupplier<InputStream, IOException> {
}
