package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import com.jgrouse.datasets.input.DataSetMetadataElementFactory;
import org.apache.poi.ss.usermodel.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.jgrouse.util.Assert.isTrue;

public class PoiDataSetMetadataBuilder /*implements DataSetMetadataBuilder*/ {

    private final Workbook workbook;
    private final DataSetMetadataElementFactory dataSetMetadataElementFactory;

    public PoiDataSetMetadataBuilder(final Workbook workbook,
                                     final DataSetMetadataElementFactory dataSetMetadataElementFactory) {
        this.workbook = workbook;
        this.dataSetMetadataElementFactory = dataSetMetadataElementFactory;
    }


    //    @Override
    @NotNull
    public Map<String, DataSetMetadata> build() {
        return StreamSupport
                .stream(workbook.spliterator(), false)
                .map(this::createMetadataForSheet)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(DataSetMetadata::getName, Function.identity()));
    }

    private DataSetMetadata createMetadataForSheet(final Sheet sheet) {
        final Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            return null;
        }
        final List<DataSetMetadataElement> elements = createMetadataElements(headerRow);
        if (elements.isEmpty()) {
            return null;
        }
        final String name = sheet.getSheetName();
        return new DataSetMetadata(elements, name);
    }

    private List<DataSetMetadataElement> createMetadataElements(final Row headerRow) {
        final List<DataSetMetadataElement> elements = new ArrayList<>(headerRow.getLastCellNum());
        final Set<String> columnNames = new HashSet<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            final Cell cell = headerRow.getCell(i);
            if (isRowTerminatedAtCell(cell)) {
                break;
            }
            final DataSetMetadataElement metadataElement = dataSetMetadataElementFactory.create(cell.getStringCellValue());
            isTrue(columnNames.add(metadataElement.getName()), () -> "Duplicated column " + metadataElement.getName());
            elements.add(metadataElement);
        }
        return elements;
    }

    private boolean isRowTerminatedAtCell(final Cell cell) {
        if (cell == null) {
            return true;
        }
        final CellType cellType = cell.getCellType();
        if (cellType == CellType.BLANK) {
            return true;
        }
        if (cellType != CellType.STRING) {
            throw new PoiParsingException(cell, "Expected cell type to be STRING, got " + cellType);
        }
        return false;
    }
}
