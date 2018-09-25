package com.jgrouse.util;

@SuppressWarnings("WeakerAccess")
public class AssertionException extends RuntimeException {

  public AssertionException() {}

  public AssertionException(String message) {
    super(message);
  }

  public AssertionException(String message, Throwable cause) {
    super(message, cause);
  }

  public AssertionException(Throwable cause) {
    super(cause);
  }

  public AssertionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
