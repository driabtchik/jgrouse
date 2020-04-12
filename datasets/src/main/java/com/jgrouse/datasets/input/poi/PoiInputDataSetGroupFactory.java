package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.input.DataSetMetadataFactory;
import com.jgrouse.datasets.input.HeaderAwareDataSetMetadataFactoryImpl;
import com.jgrouse.datasets.input.HeaderParsingInputDataSetMetadataElementFactory;
import com.jgrouse.datasets.input.poi.converters.Converters;
import com.jgrouse.util.io.InputStreamSupplier;

import java.util.List;

public class PoiInputDataSetGroupFactory {

    private final DataSetMetadataFactory<PoiSheetInputDataSet> datasetMetadataFactory;
    private final CellValueExtractorFactory cellValueExtractorFactory;

    public PoiInputDataSetGroupFactory() {
        datasetMetadataFactory =
                new HeaderAwareDataSetMetadataFactoryImpl<>(new HeaderParsingInputDataSetMetadataElementFactory());
        cellValueExtractorFactory = (metadata) ->
                new DelegatingMetadataBasedCellValueExtractor(metadata, getTypeConverters());

    }

    public PoiInputDataSetGroup create(final InputStreamSupplier inputStreamSupplier) {
        return new PoiInputDataSetGroup(inputStreamSupplier, datasetMetadataFactory, cellValueExtractorFactory);
    }

    public List<PoiTypeConverter<?>> getTypeConverters() {
        return Converters.getKnownConverters();
    }
}
