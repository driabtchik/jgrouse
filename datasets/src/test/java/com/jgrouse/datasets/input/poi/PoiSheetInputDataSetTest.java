package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class PoiSheetInputDataSetTest {

    private static final String HEADER_1 = "col1";
    private static final String HEADER_2 = "col2";
    private final DummySheet sheet = createSheet();

    private final CellValueExtractor cellValueExtractor = mock(CellValueExtractor.class);

    private final DataSetMetadata dataSetMetadata = new DataSetMetadata(Arrays.asList(
            new DataSetMetadataElement("foo", JDBCType.BIGINT),
            new DataSetMetadataElement("bar", JDBCType.BOOLEAN)
    ), "dataSetName");


    @Test
    public void stream() {

        when(cellValueExtractor.getCellValue(isA(DummyCell.class))).thenAnswer(x -> {
            final DummyCell cell = x.getArgument(0);
            final Object rawValue = cell.rawValue();
            return rawValue instanceof Double ? ((Double) rawValue) + 1.0 : rawValue.toString() + "baz";
        });

        final PoiSheetInputDataSet dataSet = new PoiSheetInputDataSet(sheet,
                (ds, name) -> dataSetMetadata, ds -> cellValueExtractor);

        assertThat(dataSet.getMetadata()).isSameAs(dataSetMetadata);
        assertThat(dataSet.stream()).containsExactly(
                Arrays.asList("foobaz", 43.0),
                Arrays.asList("barbaz", 18.0)
        );

        final ArgumentCaptor<DummyCell> processedCellsCaptor = ArgumentCaptor.forClass(DummyCell.class);
        verify(cellValueExtractor, times(4)).getCellValue(processedCellsCaptor.capture());
        assertThat(processedCellsCaptor.getAllValues().stream().map(DummyCell::rawValue).collect(Collectors.toList()))
                .containsExactly("foo", 42.0, "bar", 17.0);
    }

    @Test
    public void getHeader() {
        final PoiSheetInputDataSet dataSet = new PoiSheetInputDataSet(sheet,
                (ds, name) -> dataSetMetadata, ds -> cellValueExtractor);
        assertThat(dataSet.getHeaders()).containsExactly(HEADER_1, HEADER_2);
    }

    private DummySheet createSheet() {
        return new DummySheet("sheet",
                asList(
                        new DummyRow(asList(
                                new DummyCell(CellType.STRING).withValue(HEADER_1),
                                new DummyCell(CellType.STRING).withValue(HEADER_2),
                                new DummyCell(CellType.BLANK),
                                null,
                                new DummyCell(CellType.STRING).withValue("i am not a header")
                        )),
                        new DummyRow(
                                asList(
                                        new DummyCell(CellType.STRING).withValue("foo"),
                                        new DummyCell(CellType.NUMERIC).withValue(42.0)
                                )),
                        new DummyRow(
                                asList(
                                        new DummyCell(CellType.STRING).withValue("bar"),
                                        new DummyCell(CellType.NUMERIC).withValue(17.0),
                                        new DummyCell(CellType.NUMERIC).withValue(99.0)
                                )),
                        null
                )
        );
    }
}