package com.jgrouse.datasets;

import com.jgrouse.util.AssertionException;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class InputDataSetMetadataTest {

    private static final String AT_LEAST_ONE_ELEMENT_MUST_BE_PROVIDED = "At least one element must be provided";
    private final DataSetMetadataElement first = new DataSetMetadataElement("foo", JDBCType.BIGINT);
    private final DataSetMetadataElement second = new DataSetMetadataElement("bar", JDBCType.BIT);
    private final DataSetMetadata metadata = new DataSetMetadata(Arrays.asList(first, second), "dataSetName");


    @Test
    public void testConstructor() {
        assertThatThrownBy(() -> new DataSetMetadata(null, "dataSetName")).isInstanceOf(AssertionException.class)
                .hasMessageContaining(AT_LEAST_ONE_ELEMENT_MUST_BE_PROVIDED);
        assertThatThrownBy(() -> new DataSetMetadata(Collections.emptyList(), "dataSetName")).isInstanceOf(AssertionException.class)
                .hasMessageContaining(AT_LEAST_ONE_ELEMENT_MUST_BE_PROVIDED);

        assertThatThrownBy(() -> new DataSetMetadata(Arrays.asList(
                new DataSetMetadataElement("foo", JDBCType.BIGINT),
                null
        ), "dataSetName")).isInstanceOf(AssertionException.class).hasMessageContaining("1");

        assertThatThrownBy(() -> new DataSetMetadata(Collections.singletonList(
                new DataSetMetadataElement("foo", JDBCType.BIGINT)
        ), "")).isInstanceOf(AssertionException.class).hasMessageContaining("dataSet name");


    }

    @Test
    public void testGetElementsCount() {
        assertThat(metadata.getElementsCount()).isEqualTo(2);
    }

    @Test
    public void testGetElement() {
        assertThat(metadata.getElement(0)).isSameAs(first);
    }

    @Test
    public void testStream() {
        assertThat(metadata.stream()).containsExactly(first, second);
    }

}