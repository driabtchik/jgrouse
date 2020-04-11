package com.jgrouse.datasets.input;

import com.jgrouse.datasets.DataSetMetadata;

import javax.validation.constraints.NotNull;

@FunctionalInterface
public interface DataSetMetadataFactory<T extends InputDataSet> {
    @NotNull
    DataSetMetadata create(@NotNull T dataSet, String dataSetName);
}
