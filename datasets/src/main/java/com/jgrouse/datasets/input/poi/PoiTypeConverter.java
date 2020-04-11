package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadataElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import javax.validation.constraints.NotNull;
import java.sql.JDBCType;

public interface PoiTypeConverter<R> {
    boolean canConvert(CellType fromCellType, JDBCType toJdbcType);

    R convert(@NotNull Cell cell, DataSetMetadataElement metadataElement);
}
