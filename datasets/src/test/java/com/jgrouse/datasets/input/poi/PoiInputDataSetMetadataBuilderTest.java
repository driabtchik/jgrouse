package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import com.jgrouse.datasets.input.DataSetMetadataElementFactory;
import com.jgrouse.util.AssertionException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;
import java.sql.JDBCType;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PoiInputDataSetMetadataBuilderTest {

    private static final String SHEET_1 = "sheet1";
    private static final String SHEET_2 = "sheet2";
    private static final String SHEET_3 = "sheet3";

    private static final Workbook WORKBOOK_NO_ERRORS =
            new DummyWorkbook(
                    asList(
                            new DummySheet(
                                    SHEET_1,
                                    singletonList(
                                            new DummyRow(
                                                    asList(
                                                            new DummyCell(CellType.STRING).withValue("f1"),
                                                            new DummyCell(CellType.STRING).withValue("f2")
                                                    )
                                            )
                                    )
                            ),
                            new DummySheet(
                                    SHEET_2,
                                    singletonList(
                                            new DummyRow(
                                                    asList(
                                                            new DummyCell(CellType.STRING).withValue("f3"),
                                                            null,
                                                            new DummyCell(CellType.BOOLEAN)
                                                    )
                                            )
                                    )
                            ),
                            new DummySheet(
                                    SHEET_3,
                                    singletonList(
                                            new DummyRow(
                                                    asList(
                                                            new DummyCell(CellType.STRING).withValue("f4"),
                                                            new DummyCell(CellType.BLANK)
                                                    )
                                            )
                                    )
                            ),
                            new DummySheet(
                                    "empty header row",
                                    singletonList(new DummyRow(emptyList()))
                            ),
                            new DummySheet(
                                    "missing header row",
                                    asList(null,
                                            new DummyRow(emptyList())
                                    )
                            )
                    ));

    private static final Workbook WORKBOOK_WRONG_CELL_TYPE_IN_HEADER =
            new DummyWorkbook(
                    singletonList(new DummySheet("with error",
                                    singletonList(new DummyRow(
                                            singletonList(new DummyCell(CellType.BOOLEAN).withValue(false))))
                            )
                    )
            );

    private static final Workbook WORKBOOK_DUP_COLUMNS =
            new DummyWorkbook(
                    singletonList(
                            new DummySheet("with dup column", singletonList(new DummyRow(
                                    asList(
                                            new DummyCell(CellType.STRING).withValue("dup"),
                                            new DummyCell(CellType.STRING).withValue("dup")
                                    )
                            )))
                    ));

    private final DataSetMetadataElementFactory dataSetMetadataElementFactory =
            s -> createElement(s + "_field");

    @Test
    public void testBuild_successful() {
        PoiDataSetMetadataBuilder builder = new PoiDataSetMetadataBuilder(WORKBOOK_NO_ERRORS,
                dataSetMetadataElementFactory
        );

        Map<String, DataSetMetadata> expectedMetadata = Stream.of(new DataSetMetadata(
                        asList(
                                createElement("f1_field"),
                                createElement("f2_field")
                        ), SHEET_1),
                new DataSetMetadata(
                        singletonList(
                                createElement("f3_field")
                        ), SHEET_2),
                new DataSetMetadata(
                        singletonList(
                                createElement("f4_field")
                        ), SHEET_3)).collect(Collectors.toMap(DataSetMetadata::getName, Function.identity()));


        @NotNull Map<String, DataSetMetadata> actualMetadata = builder.build();
        assertThat(actualMetadata).containsKeys(SHEET_1, SHEET_2, SHEET_3);
        actualMetadata.forEach((name, metadata) ->
                assertThat(metadata).usingRecursiveComparison().isEqualTo(expectedMetadata.get(name))
        );
    }

    private DataSetMetadataElement createElement(String fieldName) {
        return new DataSetMetadataElement(fieldName, JDBCType.VARCHAR);
    }

    @Test
    public void testBuild_withErrors() {
        PoiDataSetMetadataBuilder builder = new PoiDataSetMetadataBuilder(WORKBOOK_WRONG_CELL_TYPE_IN_HEADER,
                dataSetMetadataElementFactory
        );
        assertThatThrownBy(builder::build).isInstanceOf(PoiParsingException.class);
    }

    @Test
    public void testBuild_dupColumns() {
        PoiDataSetMetadataBuilder builder = new PoiDataSetMetadataBuilder(WORKBOOK_DUP_COLUMNS,
                dataSetMetadataElementFactory
        );
        assertThatThrownBy(builder::build).isInstanceOf(AssertionException.class);
    }

}
