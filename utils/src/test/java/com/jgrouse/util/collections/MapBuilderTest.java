package com.jgrouse.util.collections;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

public class MapBuilderTest {

    @Test
    void build_shouldReturnEmptyMap() {
        assertThat(new MapBuilder<>().build()).isEmpty();
    }

    @Test
    void map() {
        assertThat(new MapBuilder<>().map("foo", 42).map("bar", 77).build())
                .containsOnly(entry("foo", 42),
                        entry("bar", 77));
    }

    @Test
    void statics() {
        TreeMap<String, Integer> seedMap = new TreeMap<>();
        assertThat(MapBuilder.from(seedMap).build()).isSameAs(seedMap);

        assertThat(MapBuilder.from("foo", 42).build()).containsOnly(MapEntry.entry("foo", 42));
    }

}