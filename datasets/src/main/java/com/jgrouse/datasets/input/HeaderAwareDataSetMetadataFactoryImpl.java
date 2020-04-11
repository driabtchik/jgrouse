package com.jgrouse.datasets.input;

import com.jgrouse.datasets.DataSetMetadata;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

public class HeaderAwareDataSetMetadataFactoryImpl<T extends HeaderAwareInputDataSet> implements DataSetMetadataFactory<T> {

    private final DataSetMetadataElementFactory dataSetMetadataElementFactory;

    public HeaderAwareDataSetMetadataFactoryImpl(DataSetMetadataElementFactory dataSetMetadataElementFactory) {
        this.dataSetMetadataElementFactory = dataSetMetadataElementFactory;
    }

    @Override
    public @NotNull DataSetMetadata create(@NotNull HeaderAwareInputDataSet dataSet, String dataSetName) {
        return new DataSetMetadata(dataSet.getHeaders()
                .stream()
                .map(dataSetMetadataElementFactory::create)
                .collect(Collectors.toList()),
                dataSetName);
    }
}
