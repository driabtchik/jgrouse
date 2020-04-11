package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.input.DataSetMetadataFactory;
import com.jgrouse.datasets.input.HeaderAwareInputDataSet;
import com.jgrouse.util.collections.BreakingSpliterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.jgrouse.util.Assert.notNull;

public class PoiSheetInputDataSet implements HeaderAwareInputDataSet {
    final Sheet sheet;
    private final CellValueExtractor cellValueExtractor;
    private final int metadataFieldCount;
    private final DataSetMetadata dataSetMetadata;


    public PoiSheetInputDataSet(@NotNull Sheet sheet,
                                @NotNull DataSetMetadataFactory<PoiSheetInputDataSet> dataSetMetadataFactory,
                                @NotNull CellValueExtractorFactory cellValueExtractorFactory) {
        this.sheet = notNull(sheet, "sheet must be not null");
        this.dataSetMetadata =
                notNull(dataSetMetadataFactory, "dataSetMetadataFactory must be provided")
                        .create(this, this.sheet.getSheetName());
        this.metadataFieldCount = dataSetMetadata.getElementsCount();
        this.cellValueExtractor = cellValueExtractorFactory.create(dataSetMetadata);
    }

    @Override
    public List<String> getHeaders() {
        Row row = notNull(sheet.getRow(0), () -> "Missing header row in sheet " + sheet.getSheetName());
        return StreamSupport.stream(new BreakingSpliterator<>(row.spliterator(), this::isCellPresent), false)
                .map(Cell::getStringCellValue)
                .collect(Collectors.toList());
    }

    private boolean isCellPresent(Cell cell) {
        return cell != null && cell.getCellType() != CellType.BLANK;
    }

    @Override
    @NotNull
    public Stream<List<Object>> stream() {
        return StreamSupport.stream(new BreakingSpliterator<>(sheet.spliterator(), Objects::nonNull), false)
                .skip(1)
                .map(this::extractRow);
    }

    @Override
    public @NotNull DataSetMetadata getMetadata() {
        return this.dataSetMetadata;
    }

    private List<Object> extractRow(Row row) {
        return IntStream.range(0, metadataFieldCount)
                .mapToObj(cellIndex -> extractCellValue(row, cellIndex))
                .collect(Collectors.toList());
    }

    private Object extractCellValue(Row row, int i) {
        Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cellValueExtractor.getCellValue(cell);
    }
}
