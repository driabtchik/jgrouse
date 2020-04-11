package com.jgrouse.util.collections;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BreakingSpliterator<T> implements Spliterator<T> {
    private final Spliterator<T> delegate;
    private final Predicate<T> circuitBreaker;

    public BreakingSpliterator(Spliterator<T> delegate, Predicate<T> circuitBreaker) {
        this.delegate = delegate;
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        boolean[] accepted = {true};
        boolean res = delegate.tryAdvance(t -> {
            if (circuitBreaker.test(t)) {
                action.accept(t);
            } else {
                accepted[0] = false;
            }
        });
        return res && accepted[0];
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return delegate.estimateSize();
    }

    @Override
    public long getExactSizeIfKnown() {
        return -1L;
    }

    @Override
    public int characteristics() {
        return delegate.characteristics() & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return delegate.hasCharacteristics(characteristics)
                && ((characteristics & (Spliterator.SIZED | Spliterator.SUBSIZED)) == 0);
    }

    @Override
    public Comparator<? super T> getComparator() {
        return delegate.getComparator();
    }
}
