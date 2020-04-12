package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.usermodel.Cell;

import javax.validation.constraints.NotNull;

public class PoiParsingException extends RuntimeException {

    public PoiParsingException(final String message) {
        super(message);
    }

    public PoiParsingException(@NotNull final Cell cell, final String message) {
        this(cell, message, null);
    }

    public PoiParsingException(@NotNull final Cell cell, final String message, final Throwable cause) {
        super("Error parsing cell [row = " + cell.getRow().getRowNum() + ", column = " + cell
                .getColumnIndex() + ", sheet = " + cell.getRow().getSheet().getSheetName() + "]:\n" + message, cause);
    }
}
