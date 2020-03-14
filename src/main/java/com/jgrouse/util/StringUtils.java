package com.jgrouse.util;

import javax.validation.constraints.NotNull;

public abstract class StringUtils {

    private static final String INTERPOLATION_TOKEN = "{}";

    private StringUtils() {
        // NO-OP
    }

    /**
     * Replace all {} placeholders with passed arguments. <br>
     * For example:
     * <ul>
     * <li>{@code interpolate("foo")} = "foo"</li>
     * <li>{@code interpolate("foo {}")} = "foo {}"</li>
     * <li>{@code interpolate("foo {}", (Object[]) null)} = "foo {}"</li>
     * <li>{@code interpolate("foo {}", "bar", "baz")} = "foo bar"</li>
     * <li>{@code interpolate("foo {}", null, "baz")} = "foo null bar"</li>
     * <li>{@code interpolate("foo {} {}", "bar")} = "foo bar {}"</li>
     * </ul>
     *
     * @param pattern pattern to interpolate
     * @param args    values to insert into the pattern
     * @return original string if arguments were null or empty; otherwise interpolated string
     */
    @NotNull
    @SuppressWarnings("squid:S2583") // needed to enforce the contract
    static String interpolate(final @NotNull String pattern, final Object... args) {
        if (pattern == null) {
            throw new AssertionException("interpolation pattern must be not null");
        }
        if (args == null || args.length == 0) {
            return pattern;
        }
        StringBuilder res = new StringBuilder();
        int currentPos = 0;
        int argIndex = 0;
        while (currentPos >= 0 && currentPos < pattern.length()) {
            int nextPos = pattern.indexOf(INTERPOLATION_TOKEN, currentPos);
            if (nextPos >= 0) {
                res.append(pattern, currentPos, nextPos);
                Object token = argIndex < args.length ? args[argIndex++] : INTERPOLATION_TOKEN;
                res.append(token);
                currentPos = nextPos + INTERPOLATION_TOKEN.length();
            } else {
                res.append(pattern, currentPos, pattern.length());
                break;
            }
        }
        return res.toString();
    }
}
