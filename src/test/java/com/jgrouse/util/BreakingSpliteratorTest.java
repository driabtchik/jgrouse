package com.jgrouse.util;

import static java.util.Arrays.asList;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Test;

public class BreakingSpliteratorTest {

  @Test
  public void testTryAdvance() {
    assertThat(stream(new BreakingSpliterator<>(asList(42, 15, 99, -4, 3).spliterator(), r -> r > 0), false))
                                                                                                             .containsExactly(42,
                                                                                                                              15,
                                                                                                                              99);
    assertThat(stream(new BreakingSpliterator<>(asList(42, 15, 99, -4).spliterator(), r -> r > 0), false))
                                                                                                          .containsExactly(42,
                                                                                                                           15,
                                                                                                                           99);
    assertThat(stream(new BreakingSpliterator<>(asList(-4, 3).spliterator(), r -> r > 0), false))
                                                                                                 .isEmpty();
    assertThat(stream(new BreakingSpliterator<>(Collections.<Integer> emptyList().spliterator(), r -> r > 0), false))
                                                                                                                     .isEmpty();
  }

  @Test
  public void testTrySplit() {
    assertThat(new BreakingSpliterator<>(asList(1, 2).spliterator(), r -> true).trySplit()).isNull();
  }

}