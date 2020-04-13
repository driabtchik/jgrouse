package com.jgrouse.datasets.input.poi.converters;

import com.jgrouse.datasets.DataSetMetadataElement;
import com.jgrouse.datasets.input.DataSetValueOutOfRangeException;
import com.jgrouse.datasets.input.HeaderParsingInputDataSetMetadataElementFactory;
import com.jgrouse.datasets.input.poi.PoiTypeConverter;
import com.jgrouse.util.io.InputStreamFunction;
import com.jgrouse.util.io.IoRuntimeException;
import org.apache.poi.ss.usermodel.*;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.util.List;

import static com.jgrouse.util.io.InputStreamGuard.withInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ConvertersTest {

    private static final List<PoiTypeConverter<?>> CONVERTERS = Converters.getKnownConverters();

    private final HeaderParsingInputDataSetMetadataElementFactory metadataElementFactory =
            new HeaderParsingInputDataSetMetadataElementFactory();

    private Workbook workbook;
    private FormulaEvaluator evaluator;

    @BeforeEach
    void before() {
        workbook = withInputStream(
                () -> this.getClass().getResourceAsStream("/com/jgrouse/datasets/input/poi/converters.xlsx"),
                (InputStreamFunction<Workbook>) WorkbookFactory::create);
        evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    }

    @AfterEach
    void after() {
        IoRuntimeException.unchecked(() -> workbook.close());
    }

    @ParameterizedTest
    @CsvSource({
            "0,this is string",
            "1,42.00",
            "2,TRUE",
            "3,1917/10/25 18:43:12.0",
            "4,55"
    })
    void convertToString(final int row, final String expected) {
        final Evaluator<String> evaluator = new Evaluator<>("strings", row, 1);
        assertThat(evaluator.evaluate()).isEqualTo(expected);
    }

    @Test
    void convertToString_tooLong() {
        final Evaluator<String> evaluator = new Evaluator<>("strings", 5, 1);
        assertThatExceptionOfType(DataSetValueOutOfRangeException.class)
                .isThrownBy(evaluator::evaluate)
                .satisfies(ex -> {
                    assertThat(ex).hasMessageContaining("is out of range");
                    assertThat(ex.getColumn()).isEqualTo(1);
                    assertThat(ex.getRow()).isEqualTo(5);
                    assertThat(ex.getDatasetName()).isEqualTo("strings");
                    assertThat(ex.getJdbcType()).isEqualTo(JDBCType.VARCHAR);
                    assertThat(ex.getValue().toString()).contains("long string");
                });
    }

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1, -128",
            "2, -32768",
            "3, -2147483648",
            "4, -2147483649"
    })
    void convertNumericToLong_atLowBoundry(final int row, final String expectedStr) {
        final long expected = Long.parseLong(expectedStr);
        final Evaluator<Long> evaluator = new Evaluator<>("longs", row, 1);
        assertThat(evaluator.evaluate()).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void convertNumericToLong_belowLowBoundry(final int row) {
        final Evaluator<Long> evaluator = new Evaluator<>("longs", row, 3);
        assertThatExceptionOfType(DataSetValueOutOfRangeException.class).isThrownBy(evaluator::evaluate);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void convertNumericToLong_aboveHighBoundry(final int row) {
        final Evaluator<Long> evaluator = new Evaluator<>("longs", row, 4);
        assertThatExceptionOfType(DataSetValueOutOfRangeException.class).isThrownBy(evaluator::evaluate);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "1, 127",
            "2, 32767",
            "3, 2147483647",
            "4, 2147483648"
    })
    void convertNumericToLong_atHighBoundry(final int row, final String expectedStr) {
        final long expected = Long.parseLong(expectedStr);
        final Evaluator<Long> evaluator = new Evaluator<>("longs", row, 2);
        assertThat(evaluator.evaluate()).isEqualTo(expected);
    }

    @Test
    void convertNumericToBigInt_atHighBoundry() {
        final BigInteger expected = new BigInteger("9223372036854775807");
        final Evaluator<BigInteger> evaluator = new Evaluator<>("longs", 5, 2);
        assertThat(evaluator.evaluate()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 43.32",
            "1, 213.21",
            "2, 2340000000000000"
    })
    void convertNumericToFloatingPoint(final int row, final String expectedStr) {
        final double expected = Double.parseDouble(expectedStr);
        final Evaluator<Double> evaluator = new Evaluator<>("doubles", row, 1);
        assertThat(evaluator.evaluate().doubleValue()).isCloseTo(expected, Offset.offset(1e-6));
    }

    @ParameterizedTest
    @CsvSource({
            "3, 333.44",
            "4, 5555.66",
            "5, 4443.54",
            "6, 9934344.43",
            "7, 99.99",
            "8, -99.",
            "9, 0.0099",
            "10, -0.0099"
    })
    void convertNumericToFixedPoint(final int row, final String expectedStr) {
        final BigDecimal expected = new BigDecimal(expectedStr);
        final Evaluator<BigDecimal> evaluator = new Evaluator<>("doubles", row, 1);
        assertThat(evaluator.evaluate()).isEqualByComparingTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            11,
            12,
            13,
            14
    })
    void convertNumericToFixedPoint_exceedsSize(final int row) {
        final Evaluator<BigDecimal> evaluator = new Evaluator<>("doubles", row, 1);
        assertThatExceptionOfType(DataSetValueOutOfRangeException.class).isThrownBy(evaluator::evaluate);
    }


    private final class Evaluator<T> {
        private final Sheet sheet;
        private final int row;
        private final int column;

        private Evaluator(final String sheetName, final int row, final int column) {
            this.sheet = workbook.getSheet(sheetName);
            this.row = row;
            this.column = column;
        }

        @SuppressWarnings("unchecked")
        private T evaluate() {
            final Cell cell = evaluator.evaluateInCell(sheet.getRow(row).getCell(column));
            final DataSetMetadataElement metadataElement = getMetadata(cell);
            final PoiTypeConverter<?> typeConverter = CONVERTERS.stream()
                    .filter(converter -> converter.canConvert(cell.getCellType(), metadataElement.getJdbcType()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find handler for " + cell.getCellType()));

            return (T) typeConverter.convert(cell, metadataElement);
        }

        private DataSetMetadataElement getMetadata(final Cell cell) {
            final Cell typeCell = cell.getRow().getCell(0);
            return metadataElementFactory.create(typeCell.getStringCellValue());
        }
    }


}