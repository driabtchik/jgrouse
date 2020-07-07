package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.input.DataSetMetadataFactory;
import com.jgrouse.datasets.input.InputDataSetGroup;
import com.jgrouse.util.io.InputStreamGuard;
import com.jgrouse.util.io.InputStreamSupplier;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jgrouse.util.Assert.notNull;

public class PoiInputDataSetGroup implements InputDataSetGroup {
    private final InputStreamSupplier inputStreamSupplier;
    private final DataSetMetadataFactory<PoiSheetInputDataSet> dataSetMetadataFactory;
    private final CellValueExtractorFactory cellValueExtractorFactory;
    private Workbook workbook;

    public PoiInputDataSetGroup(@NotNull final InputStreamSupplier inputStreamSupplier,
                                @NotNull final DataSetMetadataFactory<PoiSheetInputDataSet> dataSetMetadataFactory,
                                @NotNull final CellValueExtractorFactory cellValueExtractorFactory) {
        this.inputStreamSupplier = notNull(inputStreamSupplier, "inputStreamSupplier must be provided");
        this.dataSetMetadataFactory = notNull(dataSetMetadataFactory, "dataSetMetadataFactory must be provided");
        this.cellValueExtractorFactory =
                notNull(cellValueExtractorFactory, "cellValueExtractorFactory must be provided");
    }

    protected Workbook createWorkbook(final InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream, "Input stream must be provided");
        return WorkbookFactory.create(inputStream);
    }

    @Override
    public @NotNull PoiSheetInputDataSet get(@NotNull final String dataSetName) {
        notNull(dataSetName, "dataSetName must be provided");
        initializeWorkbookIfNeeded();

        return new PoiSheetInputDataSet(workbook.getSheet(dataSetName), dataSetMetadataFactory, cellValueExtractorFactory);
    }

    @Override
    public List<String> getDataSetNames() {
        initializeWorkbookIfNeeded();
        return IntStream.range(0, workbook.getNumberOfSheets())
                .mapToObj(i -> workbook.getSheetAt(i))
                .filter(sheet -> sheet.getRow(0) != null)
                .map(Sheet::getSheetName)
                .collect(Collectors.toList());
    }


    private void initializeWorkbookIfNeeded() {
        if (workbook == null) {
            workbook = InputStreamGuard.withInputStream(inputStreamSupplier, this::createWorkbook);
        }
    }

}
