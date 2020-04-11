package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;

@FunctionalInterface
public interface CellValueExtractorFactory {
    CellValueExtractor create(DataSetMetadata metadata);
}
