package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.usermodel.Cell;

import javax.validation.constraints.NotNull;

@FunctionalInterface
public interface CellValueExtractor {
    Object getCellValue(@NotNull Cell cell);
}
