package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import static java.sql.JDBCType.DECIMAL;
import static java.sql.JDBCType.VARCHAR;
import static java.util.Collections.singleton;
import static org.apache.poi.ss.usermodel.CellType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DelegatingMetadataBasedCellValueExtractorTest {

    private final DataSetMetadata metadata = new DataSetMetadata(Arrays.asList(
            new DataSetMetadataElement("field1", JDBCType.BIGINT),
            new DataSetMetadataElement("field2", VARCHAR)
    ), "dataSetName");
    private final Collection<PoiTypeConverter<?>> converters = Arrays.asList(
            new DummyConverter<BigDecimal>(STRING, singleton(DECIMAL), s -> {
                throw new UnsupportedOperationException("do not call me");
            }),
            new DummyConverter<>(NUMERIC,
                    singleton(VARCHAR),
                    s -> Double.toString(s.getNumericCellValue())),
            new DummyConverter<>(STRING,
                    singleton(VARCHAR),
                    s -> s.getStringCellValue() + " foo!")
    );
    private final DelegatingMetadataBasedCellValueExtractor extractor =
            new DelegatingMetadataBasedCellValueExtractor(metadata, converters);

    @Test
    public void testGetCellValue_normal() {
        Cell targetCell = cellInSheet(new DummyCell(STRING).withValue("foo"));
        assertThat(extractor.getCellValue(targetCell)).isEqualTo("foo foo!");
    }

    @Test
    public void testGetCellValue_noConverter() {
        Cell targetCell = cellInSheet(new DummyCell(BOOLEAN).withValue(false));
        assertThatThrownBy(() -> extractor.getCellValue(targetCell))
                .isInstanceOf(PoiParsingException.class)
                .hasMessageContaining("field2")
                .hasMessageContaining("row=1")
                .hasMessageContaining("sheet=barsheet")
                .hasMessageContaining("col=1")
                .hasMessageContaining("jdbcType=VARCHAR")
                .hasMessageContaining("cellType=BOOLEAN");
    }

    @Test
    public void testGetCellValue_cellTypeError() {
        Cell targetCell = cellInSheet(new DummyCell(CellType.ERROR));
        assertThatThrownBy(() -> extractor.getCellValue(targetCell)).isInstanceOf(PoiParsingException.class)
                .hasMessageContaining("row=1")
                .hasMessageContaining("sheet=barsheet")
                .hasMessageContaining("col=1")
                .hasMessageContaining("jdbcType=VARCHAR")
                .hasMessageContaining("cellType=ERROR");
    }

    private Cell cellInSheet(Cell targetCell) {
        DummyRow row = new DummyRow(Arrays.asList(new DummyCell(CellType.BLANK), targetCell));
        new DummySheet("barsheet", Arrays.asList(new DummyRow(Collections.emptyList()), row));
        return targetCell;
    }

    @Test
    public void testGetCellValue_nullLiteral_nullableColumn() {
        metadata.getElement(1).setNullable(true);
        Cell targetCell = cellInSheet(new DummyCell(STRING).withValue("<NULL>"));
        assertThat(extractor.getCellValue(targetCell)).isNull();
    }

    @Test
    public void testGetCellValue_notString_notNull() {
        Cell targetCell = cellInSheet(new DummyCell(NUMERIC).withValue(42.0));
        assertThat(extractor.getCellValue(targetCell)).isEqualTo("42.0");
    }

    @Test
    public void testGetCellValue_nullLiteral_notNullColumn() {
        Cell targetCell = cellInSheet(new DummyCell(STRING).withValue("<NULL>"));
        assertThatThrownBy(() -> extractor.getCellValue(targetCell)).isInstanceOf(PoiParsingException.class)
                .hasMessageContaining("Column declared as not-nullable, but the cell value is <NULL>/blank");
    }

    private static class DummyConverter<R> implements PoiTypeConverter<R> {
        private final CellType cellType;
        private final Set<JDBCType> jdbcTypes;
        private final Function<Cell, R> convert;

        public DummyConverter(CellType cellType, Set<JDBCType> jdbcTypes, Function<Cell, R> convert) {
            this.cellType = cellType;
            this.jdbcTypes = jdbcTypes;
            this.convert = convert;
        }

        @Override
        public R convert(Cell cell, DataSetMetadataElement metadataElement) {
            return convert.apply(cell);
        }

        @Override
        public boolean canConvert(CellType fromCellType, JDBCType toJdbcType) {
            return cellType.equals(fromCellType) && jdbcTypes.contains(toJdbcType);
        }
    }

}