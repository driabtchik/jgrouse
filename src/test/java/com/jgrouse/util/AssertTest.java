package com.jgrouse.util;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jgrouse.util.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("rawtypes")
public class AssertTest {

    private static final String EXPECTED_ERROR_MESSAGE = "error message";

    private static final String EMPTY_VALUE_ERROR_MESSAGE = "value expected to be not null and not empty";

    private static final Supplier<String> NOT_SUPPOSED_TO_BE_INVOKED_SUPPLIER = () -> {
        throw new IllegalArgumentException("Not supposed to be invoked");
    };

    private static final Function<Integer, String> NOT_SUPPOSED_TO_BE_INVOKED_FUNCTION = i -> {
        throw new IllegalArgumentException("Not supposed to be invoked");
    };

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testIsTrue() {
        isTrue(true, NOT_SUPPOSED_TO_BE_INVOKED_SUPPLIER);
        assertThatThrownBy(() -> isTrue(false, () -> EXPECTED_ERROR_MESSAGE))
                .isInstanceOf(AssertionException.class)
                .hasMessage(EXPECTED_ERROR_MESSAGE);

        assertThatThrownBy(() -> isTrue(false, "foo {}", "bar"))
                .isInstanceOf(AssertionException.class)
                .hasMessage("foo bar");
    }

    @Test
    public void testIsFalse() {
        isFalse(false, NOT_SUPPOSED_TO_BE_INVOKED_SUPPLIER);
        assertThatThrownBy(() -> isFalse(true, () -> EXPECTED_ERROR_MESSAGE))
                .isInstanceOf(AssertionException.class)
                .hasMessage(EXPECTED_ERROR_MESSAGE);

        assertThatThrownBy(() -> isFalse(true, "foo{}", "bar"))
                .isInstanceOf(AssertionException.class)
                .hasMessage("foobar");
    }

    @Test
    public void testNotNull() {
        String arg = "foo";
        assertThat(notNull(arg, NOT_SUPPOSED_TO_BE_INVOKED_SUPPLIER)).isSameAs(arg);
        assertThatThrownBy(() -> notNull(null, () -> "value expected to be not null")).isInstanceOf(
                AssertionException.class);

        assertThatThrownBy(() -> notNull(null, "foo{}", "bar")).isInstanceOf(
                AssertionException.class)
                .hasMessage("foobar");
    }

    @Test
    public void testIsNull() {
        isNull(null, () -> {
            throw new IllegalArgumentException("Should not be triggered");
        });

        final String errorMessage = "must not be null";
        assertThatThrownBy(() -> isNull("foobar", () -> errorMessage))
                .isInstanceOf(AssertionException.class)
                .hasMessageContaining(errorMessage);

        assertThatThrownBy(() -> isNull("something", "foo{}", "bar"))
                .isInstanceOf(AssertionException.class)
                .hasMessageContaining("foobar");
    }

    @Test
    public void testNotEmpty_collection() {
        List<String> arg = Collections.singletonList("fooo");
        assertThat(notEmpty(arg, NOT_SUPPOSED_TO_BE_INVOKED_SUPPLIER)).isSameAs(arg);

        assertThatThrownBy(() -> notEmpty((Collection) null, () -> EMPTY_VALUE_ERROR_MESSAGE))
                .isInstanceOf(AssertionException.class)
                .hasMessage(EMPTY_VALUE_ERROR_MESSAGE);

        assertThatThrownBy(() -> notEmpty(Collections.emptySet(), () -> EMPTY_VALUE_ERROR_MESSAGE))
                .isInstanceOf(AssertionException.class)
                .hasMessage(EMPTY_VALUE_ERROR_MESSAGE);

        assertThatThrownBy(() -> notEmpty(Collections.emptySet(), "foo{}", "bar"))
                .isInstanceOf(AssertionException.class)
                .hasMessage("foobar");
    }

    @Test
    public void testNotNullElements() {
        List<String> args = Arrays.asList("foo", "bar");
        assertThat(notNullElements(args, NOT_SUPPOSED_TO_BE_INVOKED_FUNCTION)).isSameAs(args);
        assertThatThrownBy(() -> notNullElements(null, NOT_SUPPOSED_TO_BE_INVOKED_FUNCTION))
                .isInstanceOf(AssertionException.class);

        assertThatThrownBy(() -> notNullElements(Arrays.asList("foo", null, "bar"), i -> "expected not null value at " + i))
                .isInstanceOf(AssertionException.class);

    }

    @Test
    public void testNotEmpty_map() {
        Map<String, String> arg = Collections.singletonMap("foo", "bar");
        assertThat(notEmpty(arg, NOT_SUPPOSED_TO_BE_INVOKED_SUPPLIER)).isSameAs(arg);
        assertThatThrownBy(() -> notEmpty((Map) null, () -> EMPTY_VALUE_ERROR_MESSAGE))
                .isInstanceOf(AssertionException.class)
                .hasMessage(EMPTY_VALUE_ERROR_MESSAGE);

        assertThatThrownBy(() -> notEmpty(Collections.emptyMap(), () -> EMPTY_VALUE_ERROR_MESSAGE))
                .isInstanceOf(AssertionException.class)
                .hasMessage(EMPTY_VALUE_ERROR_MESSAGE);

        assertThatThrownBy(() -> notEmpty(Collections.emptyMap(), "foo{}", "bar"))
                .isInstanceOf(AssertionException.class)
                .hasMessage("foobar");

    }

    @Test
    public void testNotEmpty_string() {
        String val = "foo";
        assertThat(notEmpty(val, NOT_SUPPOSED_TO_BE_INVOKED_SUPPLIER)).isSameAs(val);
        assertThatThrownBy(() -> notEmpty((String) null, () -> EMPTY_VALUE_ERROR_MESSAGE))
                .isInstanceOf(AssertionException.class)
                .hasMessage(EMPTY_VALUE_ERROR_MESSAGE);

        assertThatThrownBy(() -> notEmpty("", () -> EMPTY_VALUE_ERROR_MESSAGE))
                .isInstanceOf(AssertionException.class)
                .hasMessage(EMPTY_VALUE_ERROR_MESSAGE);

        assertThatThrownBy(() -> notEmpty("", "foo{}", "bar"))
                .isInstanceOf(AssertionException.class)
                .hasMessage("foobar");
    }

}
