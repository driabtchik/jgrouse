package com.jgrouse.datasets;

import com.jgrouse.datasets.input.DataSetMetadataElementFactory;
import com.jgrouse.datasets.input.HeaderAwareDataSetMetadataFactoryImpl;
import com.jgrouse.datasets.input.HeaderAwareInputDataSet;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HeaderAwareInputDataSetMetadataFactoryImplTest {

    private final DataSetMetadataElementFactory elementFactory =
            headerName -> new DataSetMetadataElement(headerName, JDBCType.BIGINT);
    private final HeaderAwareDataSetMetadataFactoryImpl<HeaderAwareInputDataSet> metadataFactory =
            new HeaderAwareDataSetMetadataFactoryImpl<>(elementFactory);

    @Test
    void create() {
        final HeaderAwareInputDataSet dataSet = mock(HeaderAwareInputDataSet.class);
        when(dataSet.getHeaders()).thenReturn(Arrays.asList("foo", "bar"));
        assertThat(metadataFactory.create(dataSet, "dataSetName"))
                .usingRecursiveComparison().isEqualTo(
                new DataSetMetadata(Arrays.asList(
                        new DataSetMetadataElement("foo", JDBCType.BIGINT),
                        new DataSetMetadataElement("bar", JDBCType.BIGINT)
                ), "dataSetName")
        );
        verify(dataSet).getHeaders();
    }

}