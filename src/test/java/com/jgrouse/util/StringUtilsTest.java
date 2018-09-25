package com.jgrouse.util;

import static com.jgrouse.util.StringUtils.interpolate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

public class StringUtilsTest {

  @Test
  public void testInterpolate() {

    // noinspection ConstantConditions false alarm
    assertThatThrownBy(() -> interpolate(null, "foo")).isInstanceOf(AssertionException.class);

    String foo = "foo {} {}";
    assertThat(interpolate(foo)).isSameAs(foo);
    assertThat(interpolate(foo, (Object[]) null)).isSameAs(foo);

    assertThat(interpolate(foo, "bar", "baz")).isEqualTo("foo bar baz");

    assertThat(interpolate(" {}{}", "foo", "bar")).isEqualTo(" foobar");
    assertThat(interpolate(" {}", "bar")).isEqualTo(" bar");
    assertThat(interpolate("{} ", "bar")).isEqualTo("bar ");
    assertThat(interpolate(" {} ", "bar")).isEqualTo(" bar ");
    assertThat(interpolate(" {} ", (String) null)).isEqualTo(" null ");

  }

}