package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.JDBCType;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class PoiSheetInputDataSetTest {

    private static final String HEADER_1 = "col1";
    private static final String HEADER_2 = "col2";
    private DummySheet sheet;

    private final CellValueExtractor cellValueExtractor = mock(CellValueExtractor.class);
    private final FormulaEvaluator formulaEvaluator = mock(FormulaEvaluator.class);

    private final CreationHelper creationHelper = mock(CreationHelper.class);

    private final DataSetMetadata dataSetMetadata = new DataSetMetadata(asList(
            new DataSetMetadataElement("foo", JDBCType.BIGINT),
            new DataSetMetadataElement("bar", JDBCType.BOOLEAN)
    ), "dataSetName");


    @BeforeEach
    void beforeEach() {
        when(creationHelper.createFormulaEvaluator()).thenReturn(formulaEvaluator);

        when(formulaEvaluator.evaluateFormulaCell(isA(Cell.class))).thenAnswer(x -> {
            DummyCell cell = x.getArgument(0);
            Object val = cell.rawValue();
            if (val instanceof String) {
                cell.withValue("evaluated-" + val);
                return CellType.STRING;
            } else if (val instanceof Number) {
                cell.withValue(1000 * ((Number) val).doubleValue());
                return CellType.NUMERIC;
            }
            throw new IllegalArgumentException("not applicable");
        });
        sheet = createSheet();
    }

    @Test
    public void stream() {

        when(cellValueExtractor.getCellValue(isA(Cell.class))).thenAnswer(x -> {
            final Cell cell = x.getArgument(0);
            return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() + "baz" : cell.getNumericCellValue() + 1.0;
        });

        final PoiSheetInputDataSet dataSet = new PoiSheetInputDataSet(sheet,
                (ds, name) -> dataSetMetadata, ds -> cellValueExtractor);

        assertThat(dataSet.getMetadata()).isSameAs(dataSetMetadata);
        assertThat(dataSet.stream()).containsExactly(
                asList("foobaz", 43.0),
                asList("barbaz", 18.0),
                asList("evaluated-formulabaz", 11001.0)
        );

        final ArgumentCaptor<Cell> processedCellsCaptor = ArgumentCaptor.forClass(Cell.class);
        verify(cellValueExtractor, times(6)).getCellValue(processedCellsCaptor.capture());
        assertThat(processedCellsCaptor.getAllValues().stream().map(cell ->
                cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : cell.getNumericCellValue()
        ).collect(Collectors.toList()))
                .containsExactly("foo", 42.0, "bar", 17.0, "evaluated-formula", 11000.0);
    }

    @Test
    public void getHeader() {
        final PoiSheetInputDataSet dataSet = new PoiSheetInputDataSet(sheet,
                (ds, name) -> dataSetMetadata, ds -> cellValueExtractor);
        assertThat(dataSet.getHeaders()).containsExactly(HEADER_1, HEADER_2);
    }

    private DummySheet createSheet() {
        DummySheet sheet = new DummySheet("sheet",
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
                        new DummyRow(
                                asList(
                                        new DummyCell(CellType.FORMULA).withValue("formula"),
                                        new DummyCell(CellType.FORMULA).withValue(11.0)
                                )),

                        null
                )
        );
        new DummyWorkbook(Collections.singletonList(sheet)).withCreationHelper(creationHelper);
        return sheet;

    }
}