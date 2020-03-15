package com.jgrouse.util.collections;


import org.junit.jupiter.api.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;

public class BreakingSpliteratorTest {

    @Test
    void tryAdvance() {
        assertThat(stream(new BreakingSpliterator<>(asList(42, 15, 99, -4, 3).spliterator(), r -> r > 0), false))
                .containsExactly(42, 15, 99);
        assertThat(stream(new BreakingSpliterator<>(asList(42, 15, 99, -4).spliterator(), r -> r > 0), false))
                .containsExactly(42, 15, 99);
        assertThat(stream(new BreakingSpliterator<>(asList(-4, 3).spliterator(), r -> r > 0), false))
                .isEmpty();
        //noinspection RedundantOperationOnEmptyContainer -- explicitly testing for empty lists
        assertThat(stream(new BreakingSpliterator<>(Collections.<Integer>emptyList().spliterator(), r -> r > 0), false))
                .isEmpty();
    }

    @Test
    void trySplit() {
        assertThat(new BreakingSpliterator<>(asList(1, 2).spliterator(), r -> true).trySplit()).isNull();
    }

}