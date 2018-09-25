package com.jgrouse.util;

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
    boolean[] interrupted = { false };
    boolean res = delegate.tryAdvance(t -> {
      if (circuitBreaker.test(t)) {
        action.accept(t);
      } else {
        interrupted[0] = true;
      }
    });
    return res || interrupted[0];
  }

  @Override
  public void forEachRemaining(Consumer<? super T> action) {
    delegate.forEachRemaining(action);
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
    return delegate.getExactSizeIfKnown();
  }

  @Override
  public int characteristics() {
    return delegate.characteristics();
  }

  @Override
  public boolean hasCharacteristics(int characteristics) {
    return delegate.hasCharacteristics(characteristics);
  }

  @Override
  public Comparator<? super T> getComparator() {
    return delegate.getComparator();
  }
}
