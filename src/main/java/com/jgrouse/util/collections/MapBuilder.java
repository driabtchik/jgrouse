package com.jgrouse.util.collections;

import static com.jgrouse.util.Assert.notNull;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

public class MapBuilder<K, V> {

  private Map<K, V> underlier = new HashMap<>();

  @NotNull
  public Map<K, V> build() {
    return underlier;
  }

  @NotNull
  public MapBuilder<K, V> map(@NotNull K key, V value) {
    underlier.put(notNull(key, "key must be provided"), value);
    return this;
  }

  @NotNull
  public static <K, V> MapBuilder<K, V> from(@NotNull Map<K, V> seed) {
    MapBuilder<K, V> mapBuilder = new MapBuilder<>();
    mapBuilder.underlier = notNull(seed, "seed must be provided");
    return mapBuilder;
  }

  @NotNull
  public static <K, V> MapBuilder<K, V> from(@NotNull K key, V value) {
    return new MapBuilder<K, V>().map(key, value);
  }
}
