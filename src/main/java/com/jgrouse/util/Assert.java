package com.jgrouse.util;

import static com.jgrouse.util.StringUtils.interpolate;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

@SuppressWarnings("UnusedReturnValue")
public interface Assert {

  static void isTrue(final boolean value, final Supplier<String> messageSupplier) {
    if (!value) {
      throw new AssertionException(messageSupplier.get());
    }
  }

  static void isTrue(final boolean value, @NotNull final String pattern, final Object... args) {
    isTrue(value, () -> interpolate(pattern, args));
  }

  static void isFalse(final boolean value, @NotNull final Supplier<String> messageSupplier) {
    isTrue(!value, messageSupplier);
  }

  static void isFalse(final boolean value, @NotNull final String pattern, final Object... args) {
    isTrue(!value, () -> interpolate(pattern, args));
  }

  @NotNull
  static <T> T notNull(final T expr, @NotNull final Supplier<String> messageSupplier) {
    isTrue(expr != null, messageSupplier);
    return expr;
  }

  @NotNull
  static <T> T notNull(final T expr, @NotNull final String pattern, final Object... args) {
    return notNull(expr, () -> interpolate(pattern, args));
  }

  static void isNull(Object expr, @NotNull final Supplier<String> messageSupplier) {
    isTrue(expr == null, messageSupplier);
  }

  static void isNull(Object expr, @NotNull final String pattern, final Object... args) {
    isNull(expr, () -> interpolate(pattern, args));
  }

  @NotNull
  static <T extends Collection> T notEmpty(final T expr, final @NotNull Supplier<String> messageSupplier) {
    isTrue(expr != null && !expr.isEmpty(), messageSupplier);
    return expr;
  }

  @NotNull
  static <T extends Collection> T notEmpty(final T expr, @NotNull final String pattern, final Object... args) {
    return notEmpty(expr, () -> interpolate(pattern, args));
  }

  @NotNull
  static <T extends Collection> T notNullElements(@NotNull final T expr,
                                                  @NotNull Function<Integer, String> messageSupplierForNullElement) {
    notNull(expr, "Collection must be provided");
    int[] i = { 0 };
    for (Object o : expr) {
      notNull(o, () -> messageSupplierForNullElement.apply(i[0]));
      i[0]++;
    }
    return expr;
  }

  @NotNull
  static <T extends Map> T notEmpty(final T expr, final @NotNull Supplier<String> messageSupplier) {
    isTrue(expr != null && !expr.isEmpty(), messageSupplier);
    return expr;
  }

  @NotNull
  static <T extends Map> T notEmpty(final T expr, @NotNull final String pattern, final Object... args) {
    return notEmpty(expr, () -> interpolate(pattern, args));
  }

  @NotNull
  static CharSequence notEmpty(final CharSequence expr, final @NotNull Supplier<String> messageSupplier) {
    isTrue(expr != null && expr.length() > 0, messageSupplier);
    return expr;
  }

  @NotNull
  static CharSequence notEmpty(final CharSequence expr, @NotNull final String pattern, final Object... args) {
    return notEmpty(expr, () -> interpolate(pattern, args));
  }

}
