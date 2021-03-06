package com.jgrouse.util.collections;


import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BreakingSpliteratorTest {

    public static final int FULL_MASK = 0xFFFFFFFF;

    @Test
    void tryAdvance_skipsAllElementsAfterFiltered() {
        assertThat(stream(new BreakingSpliterator<>(asList(42, 15, 99, -4, 3).spliterator(), r -> r > 0), false))
                .containsExactly(42, 15, 99);
    }

    @Test
    void tryAdvance_skipFilteredElement() {
        assertThat(stream(new BreakingSpliterator<>(asList(42, 15, 99, -4).spliterator(), r -> r > 0), false))
                .containsExactly(42, 15, 99);
    }

    @Test
    void tryAdvance_emptyStreamIfFirstElementFiltered() {
        assertThat(stream(new BreakingSpliterator<>(asList(-4, 3).spliterator(), r -> r > 0), false))
                .isEmpty();
    }

    @Test
    void tryAdvance_worksOnEmptyStream() {
        //noinspection RedundantOperationOnEmptyContainer -- explicitly testing for empty lists
        assertThat(stream(new BreakingSpliterator<>(Collections.<Integer>emptyList().spliterator(), r -> r > 0), false))
                .isEmpty();
    }

    @Test
    void trySplit() {
        assertThat(new BreakingSpliterator<>(asList(1, 2).spliterator(), r -> true).trySplit()).isNull();
    }

    @Test
    void delegatesForEachRemaining() {
        final Spliterator<Integer> delegate = asList(42, 44, -3, 2).spliterator();
        final Set<Integer> buffer = new HashSet<>();

        new BreakingSpliterator<>(delegate, r -> r > 0).forEachRemaining(buffer::add);
        assertThat(buffer).containsExactlyInAnyOrder(42, 44);
    }

    @SuppressWarnings("unchecked")
    @Test
    void delegatesForEstimateSize() {
        final Spliterator<String> delegate = mock(Spliterator.class);
        when(delegate.estimateSize()).thenReturn(42L);
        assertThat(new BreakingSpliterator<>(delegate, r -> true).estimateSize()).isEqualTo(42L);
    }

    @SuppressWarnings("unchecked")
    @Test
    void cannotProvideExactSize() {
        final Spliterator<String> delegate = mock(Spliterator.class);
        when(delegate.getExactSizeIfKnown()).thenThrow(new IllegalArgumentException("do not call me"));
        assertThat(new BreakingSpliterator<>(delegate, r -> true).getExactSizeIfKnown()).isEqualTo(-1L);
    }

    @Test
    void useDelegatesComparator() {
        final Spliterator<String> delegate = Stream.of("foo", "bar").sorted().spliterator();
        assertThat(new BreakingSpliterator<>(delegate, r -> true).getComparator()).isSameAs(delegate.getComparator());

    }

    @SuppressWarnings({"unchecked", "MagicConstant"})
    @Test
    void characteristicsExcludeSized() {
        final Spliterator<String> delegate = mock(Spliterator.class);
        when(delegate.characteristics()).thenReturn(FULL_MASK);
        when(delegate.hasCharacteristics(anyInt())).thenReturn(true);
        final int expectedCharacteristics = FULL_MASK ^ (Spliterator.SIZED | Spliterator.SUBSIZED);
        final BreakingSpliterator<String> spliterator = new BreakingSpliterator<>(delegate, r -> true);
        assertThat(spliterator.characteristics()).isEqualTo(expectedCharacteristics);
        assertThat(spliterator.hasCharacteristics(Spliterator.SIZED)).isFalse();
        assertThat(spliterator.hasCharacteristics(Spliterator.SUBSIZED)).isFalse();
        assertThat(spliterator.hasCharacteristics(Spliterator.SUBSIZED |
                        Spliterator.SIZED |
                        Spliterator.CONCURRENT |
                        Spliterator.DISTINCT |
                        Spliterator.IMMUTABLE |
                        Spliterator.NONNULL |
                        Spliterator.ORDERED |
                        Spliterator.SORTED
                )).isFalse();

        assertThat(spliterator.hasCharacteristics(
                Spliterator.SIZED |
                Spliterator.CONCURRENT |
                Spliterator.DISTINCT |
                Spliterator.IMMUTABLE |
                Spliterator.NONNULL |
                Spliterator.ORDERED |
                Spliterator.SORTED
        )).isFalse();

        assertThat(spliterator.hasCharacteristics(
                        Spliterator.CONCURRENT |
                        Spliterator.DISTINCT |
                        Spliterator.IMMUTABLE |
                        Spliterator.NONNULL |
                        Spliterator.ORDERED |
                        Spliterator.SORTED
        )).isTrue();

    }

}