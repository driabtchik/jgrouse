package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.input.DataSetMetadataFactory;
import com.jgrouse.datasets.input.InputDataSetGroup;
import com.jgrouse.util.io.InputStreamGuard;
import com.jgrouse.util.io.InputStreamSupplier;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static com.jgrouse.util.Assert.notNull;

public class PoiInputDataSetGroup implements InputDataSetGroup {
    private final InputStreamSupplier inputStreamSupplier;
    private final DataSetMetadataFactory<PoiSheetInputDataSet> dataSetMetadataFactory;
    private final CellValueExtractorFactory cellValueExtractorFactory;
    private Workbook workbook;

    public PoiInputDataSetGroup(@NotNull InputStreamSupplier inputStreamSupplier,
                                @NotNull DataSetMetadataFactory<PoiSheetInputDataSet> dataSetMetadataFactory,
                                @NotNull CellValueExtractorFactory cellValueExtractorFactory) {
        this.inputStreamSupplier = notNull(inputStreamSupplier, "inputStreamSupplier must be provided");
        this.dataSetMetadataFactory = notNull(dataSetMetadataFactory, "dataSetMetadataFactory must be provided");
        this.cellValueExtractorFactory =
                notNull(cellValueExtractorFactory, "cellValueExtractorFactory must be provided");
    }

    protected Workbook createWorkbook(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream, "Input stream must be provided");
        return WorkbookFactory.create(inputStream);
    }

    @Override
    public @NotNull PoiSheetInputDataSet get(@NotNull String dataSetName) {
        notNull(dataSetName, "dataSetName must be provided");
        if (workbook == null) {
            workbook = InputStreamGuard.withInputStream(inputStreamSupplier, this::createWorkbook);
        }

        return new PoiSheetInputDataSet(workbook.getSheet(dataSetName), dataSetMetadataFactory, cellValueExtractorFactory);
    }

}
