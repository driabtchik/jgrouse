package com.jgrouse.datasets.input.poi.converters;

import com.jgrouse.datasets.DataSetMetadataElement;
import com.jgrouse.datasets.input.DataSetValueOutOfRangeException;
import com.jgrouse.datasets.input.poi.PoiTypeConverter;
import com.jgrouse.util.collections.MapBuilder;
import org.apache.commons.lang3.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.sql.JDBCType.*;

public abstract class Converters {

    private Converters() {
        //NO-OP
    }

    public static List<PoiTypeConverter<?>> getKnownConverters() {
        return Stream.concat(
                Stream.of(
                        new ToStringConverter(),
                        new StringToBigIntConverter(),
                        new NumericToFloatingPointConverter(),
                        new NumericToFixedPointConverter(),
                        new StringToFixedPointConverter()
                ),
                NumericToLongConverter.KNOWN_CONVERTERS.stream()
        ).collect(Collectors.toList());
    }

    private static DataSetValueOutOfRangeException createException(@NotNull Cell cell, Object value, JDBCType jdbcType) {
        return new DataSetValueOutOfRangeException(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), jdbcType, value);
    }

    public static class ToStringConverter implements PoiTypeConverter<String> {
        private static final Set<JDBCType> CHARACTER_JDBC_TYPES = EnumSet.of(
                CHAR,
                VARCHAR,
                LONGVARCHAR,
                CLOB,
                NCHAR,
                NVARCHAR,
                LONGNVARCHAR,
                NCLOB
        );
        private final DataFormatter dataFormatter = new DataFormatter();

        @Override
        public boolean canConvert(CellType fromCellType, JDBCType toJdbcType) {
            return CHARACTER_JDBC_TYPES.contains(toJdbcType);
        }

        @Override
        public String convert(@NotNull Cell cell, DataSetMetadataElement metadataElement) {
            String result = dataFormatter.formatCellValue(cell);
            if (metadataElement.getLength() > 0 && result.length() > metadataElement.getLength()) {
                throw createException(cell, result, metadataElement.getJdbcType());
            }
            return result;
        }
    }

    public static class NumericToLongConverter implements PoiTypeConverter<Long> {
        private final JDBCType handledType;
        private final long minValue;
        private final long maxValue;


        private static final Map<JDBCType, Range<Long>> INTEGER_JDBC_TYPES =
                MapBuilder.from(BIT, Range.between(0L, 1L))
                        .map(TINYINT, Range.between((long) Byte.MIN_VALUE, (long) Byte.MAX_VALUE))
                        .map(SMALLINT, Range.between((long) Short.MIN_VALUE, (long) Short.MAX_VALUE))
                        .map(INTEGER, Range.between((long) Integer.MIN_VALUE, (long) Integer.MAX_VALUE))
                        .map(BIGINT, Range.between(Long.MIN_VALUE, Long.MAX_VALUE))
                        .build();

        private static final List<NumericToLongConverter> KNOWN_CONVERTERS = Arrays.asList(
                new NumericToLongConverter(BIT, 0L, 1L),
                new NumericToLongConverter(TINYINT, Byte.MIN_VALUE, Byte.MAX_VALUE),
                new NumericToLongConverter(SMALLINT, Short.MIN_VALUE, Short.MAX_VALUE),
                new NumericToLongConverter(INTEGER, Integer.MIN_VALUE, Integer.MAX_VALUE),
                new NumericToLongConverter(BIGINT, Long.MIN_VALUE, Long.MAX_VALUE)
        );

        private NumericToLongConverter(JDBCType handledType, long minValue, long maxValue) {
            this.handledType = handledType;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public static List<NumericToLongConverter> getKnownConverters() {
            return KNOWN_CONVERTERS;
        }


        @Override
        public boolean canConvert(CellType fromCellType, JDBCType toJdbcType) {
            return CellType.NUMERIC == fromCellType && handledType == toJdbcType;
        }

        @Override
        public Long convert(@NotNull Cell cell, DataSetMetadataElement metadataElement) {
            double cellValue = cell.getNumericCellValue();
            long truncated = BigDecimal.valueOf(cellValue).longValue();
            if (truncated < minValue || truncated > maxValue) {
                throw createException(cell, truncated, handledType);
            }
            return truncated;
        }

    }

    public static class StringToBigIntConverter implements PoiTypeConverter<BigInteger> {
        @Override
        public boolean canConvert(CellType fromCellType, JDBCType toJdbcType) {
            return CellType.STRING == fromCellType && BIGINT == toJdbcType;
        }

        @Override
        public BigInteger convert(@NotNull Cell cell, DataSetMetadataElement metadataElement) {
            String cellValue = cell.getStringCellValue();
            return new BigInteger(cellValue);
        }
    }

    public static class NumericToFloatingPointConverter implements PoiTypeConverter<Double> {
        private static final Set<JDBCType> HANDLED_TYPES = EnumSet.of(
                FLOAT,
                REAL,
                DOUBLE
        );

        @Override
        public boolean canConvert(CellType fromCellType, JDBCType toJdbcType) {
            return CellType.NUMERIC == fromCellType && HANDLED_TYPES.contains(toJdbcType);
        }

        @Override
        public Double convert(@NotNull Cell cell, DataSetMetadataElement metadataElement) {
            return cell.getNumericCellValue();
        }
    }

    private abstract static class BaseToFixedPointConverter implements PoiTypeConverter<BigDecimal> {

        protected abstract BigDecimal extractValue(Cell cell);

        @Override
        public BigDecimal convert(@NotNull Cell cell, DataSetMetadataElement metadataElement) {
            BigDecimal result = extractValue(cell);
            if (metadataElement.getLength() >= 0) {
                int scale = metadataElement.getScale();
                if (scale < 0) {
                    scale = 0;
                }
                int effective = metadataElement.getLength() - scale;
                BigDecimal maxPermittedValue = BigDecimal.ONE.movePointRight(effective);
                if (result.abs().compareTo(maxPermittedValue) >= 0) {
                    throw createException(cell, result, metadataElement.getJdbcType());
                }
            }
            return result;
        }

    }

    public static class NumericToFixedPointConverter extends BaseToFixedPointConverter implements PoiTypeConverter<BigDecimal> {

        @Override
        public boolean canConvert(CellType fromCellType, JDBCType toJdbcType) {
            return CellType.NUMERIC == fromCellType &&
                    (NUMERIC == toJdbcType || DECIMAL == toJdbcType);
        }

        @Override
        protected BigDecimal extractValue(Cell cell) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }

    }

    public static class StringToFixedPointConverter extends BaseToFixedPointConverter implements PoiTypeConverter<BigDecimal> {
        @Override
        public boolean canConvert(CellType fromCellType, JDBCType toJdbcType) {
            return CellType.STRING == fromCellType &&
                    (NUMERIC == toJdbcType || DECIMAL == toJdbcType);
        }

        @Override
        protected BigDecimal extractValue(Cell cell) {
            return new BigDecimal(cell.getStringCellValue());
        }
    }


}
