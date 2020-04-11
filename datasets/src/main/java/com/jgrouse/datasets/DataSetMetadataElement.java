package com.jgrouse.datasets;

import javax.validation.constraints.NotNull;
import java.sql.JDBCType;

import static com.jgrouse.util.Assert.notNull;

public class DataSetMetadataElement {
    private final String name;
    private final JDBCType jdbcType;
    private int length = -1;
    private int scale = -1;
    private boolean nullable;

    public DataSetMetadataElement(@NotNull String name, @NotNull JDBCType jdbcType) {
        this.name = notNull(name, "name  must be provided");
        this.jdbcType = notNull(jdbcType, "jdbcType must be provided");
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public JDBCType getJdbcType() {
        return jdbcType;
    }

    public int getLength() {
        return length;
    }

    @NotNull
    public DataSetMetadataElement setLength(int length) {
        this.length = length;
        return this;
    }

    public int getScale() {
        return scale;
    }

    @NotNull
    public DataSetMetadataElement setScale(int scale) {
        this.scale = scale;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    @NotNull
    public DataSetMetadataElement setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }
}
