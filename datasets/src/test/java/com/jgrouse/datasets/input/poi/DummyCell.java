package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Calendar;
import java.util.Date;

import static com.jgrouse.util.Assert.isTrue;
import static com.jgrouse.util.Assert.notNull;

public class DummyCell implements Cell {
    private Object value;
    private final CellType cellType;
    private Row row;
    private int columnIndex;

    public DummyCell(CellType cellType) {
        this.cellType = cellType;
    }

    DummyCell fromRow(Row row) {
        this.row = row;
        return this;
    }

    DummyCell fromIndex(int columnIndex) {
        this.columnIndex = columnIndex;
        return this;
    }

    public DummyCell withValue(Object value) {
        this.value = value;
        return this;
    }

    Object rawValue() {
        return value;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public CellType getCachedFormulaResultType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public int getRowIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sheet getSheet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Row getRow() {
        return row;
    }

    @Override
    public void setCellType(CellType cellType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellType getCellTypeEnum() {
        return cellType;
    }

    @Override
    public CellType getCachedFormulaResultTypeEnum() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(Date value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(Calendar value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(RichTextString value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCellFormula() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellFormula(String formula) throws FormulaParseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getNumericCellValue() {
        return (Double) getNotNullValue();
    }

    @Override
    public Date getDateCellValue() {
        return (Date) getNotNullValue();
    }

    @Override
    public RichTextString getRichStringCellValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringCellValue() {
        isTrue(cellType == CellType.STRING || cellType == CellType.FORMULA, () -> "invalid cell type " + cellType);
        return String.valueOf(getNotNullValue());
    }

    @Override
    public void setCellValue(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellErrorValue(byte value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getBooleanCellValue() {
        return (Boolean) getNotNullValue();
    }

    private Object getNotNullValue() {
        return notNull(value, () -> "Value must be set");
    }

    @Override
    public byte getErrorCellValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellStyle getCellStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellStyle(CellStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAsActiveCell() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellAddress getAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comment getCellComment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellComment(Comment comment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeCellComment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Hyperlink getHyperlink() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHyperlink(Hyperlink link) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeHyperlink() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRangeAddress getArrayFormulaRange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPartOfArrayFormulaGroup() {
        throw new UnsupportedOperationException();
    }
}