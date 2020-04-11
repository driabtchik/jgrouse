package com.jgrouse.datasets.input;

import com.jgrouse.datasets.DataSetMetadataElement;

import javax.validation.constraints.NotNull;

@FunctionalInterface
public interface DataSetMetadataElementFactory {
    @NotNull
    DataSetMetadataElement create(@NotNull String headerName);
}
