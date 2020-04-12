package com.jgrouse.datasets.input;

import java.sql.JDBCType;

import static com.jgrouse.util.StringUtils.interpolate;

public class DataSetValueOutOfRangeException extends RuntimeException {
    private final String datasetName;
    private final int row;
    private final int column;
    private final JDBCType jdbcType;
    private final Object value;

    public DataSetValueOutOfRangeException(final String datasetName,
                                           final int row,
                                           final int column,
                                           final JDBCType jdbcType,
                                           final Object value) {
        super(interpolate("Dataset:[{}] at [{}, {}] - value[{}] is out of range for JDBCType {}", datasetName, row, column, value, jdbcType));
        this.datasetName = datasetName;
        this.row = row;
        this.column = column;
        this.jdbcType = jdbcType;
        this.value = value;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public JDBCType getJdbcType() {
        return jdbcType;
    }

    public Object getValue() {
        return value;
    }
}
