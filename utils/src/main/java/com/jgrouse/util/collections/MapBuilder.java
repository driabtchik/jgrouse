package com.jgrouse.util.collections;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static com.jgrouse.util.Assert.notNull;

public final class MapBuilder<K, V> {

    private Map<K, V> underlier = new HashMap<>();

    @NotNull
    public static <K, V> MapBuilder<K, V> from(@NotNull final Map<K, V> seed) {
        final MapBuilder<K, V> mapBuilder = new MapBuilder<>();
        mapBuilder.underlier = notNull(seed, "seed must be provided");
        return mapBuilder;
    }

    @NotNull
    public static <K, V> MapBuilder<K, V> from(@NotNull final K key, final V value) {
        return new MapBuilder<K, V>().map(key, value);
    }

    @NotNull
    public Map<K, V> build() {
        return underlier;
    }

    @NotNull
    public MapBuilder<K, V> map(@NotNull final K key, final V value) {
        underlier.put(notNull(key, "key must be provided"), value);
        return this;
    }
}
