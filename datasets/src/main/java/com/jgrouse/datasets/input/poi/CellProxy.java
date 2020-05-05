package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Removal;

import java.util.Calendar;
import java.util.Date;

public class CellProxy implements Cell {
    private final Cell delegate;
    private final CellType cellType;


    public CellProxy(final Cell delegate, final CellType cellType) {
        this.delegate = delegate;
        this.cellType = cellType;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public int getColumnIndex() {
        return delegate.getColumnIndex();
    }

    @Override
    public String getStringCellValue() {
        return delegate.getStringCellValue();
    }

    @Override
    public Row getRow() {
        return delegate.getRow();
    }

    @Override
    public int getRowIndex() {
        return delegate.getRowIndex();
    }

    @Override
    public Sheet getSheet() {
        return delegate.getSheet();
    }

    @Override
    public void setCellType(final CellType cellType) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Removal(version = "4.2")
    @Deprecated
    public CellType getCellTypeEnum() {
        return cellType;
    }

    @Override
    public CellType getCachedFormulaResultType() {
        return delegate.getCachedFormulaResultType();
    }

    @Override
    @Removal(version = "4.2")
    @Deprecated
    public CellType getCachedFormulaResultTypeEnum() {
        return delegate.getCachedFormulaResultTypeEnum();
    }

    @Override
    public void setCellValue(final double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(final Date value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(final Calendar value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(final RichTextString value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellValue(final String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellFormula(final String formula) throws FormulaParseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCellFormula() {
        return delegate.getCellFormula();
    }

    @Override
    public double getNumericCellValue() {
        return delegate.getNumericCellValue();
    }

    @Override
    public Date getDateCellValue() {
        return delegate.getDateCellValue();
    }

    @Override
    public RichTextString getRichStringCellValue() {
        return delegate.getRichStringCellValue();
    }

    @Override
    public void setCellValue(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCellErrorValue(final byte value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getBooleanCellValue() {
        return delegate.getBooleanCellValue();
    }

    @Override
    public byte getErrorCellValue() {
        return delegate.getErrorCellValue();
    }

    @Override
    public void setCellStyle(final CellStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellStyle getCellStyle() {
        return delegate.getCellStyle();
    }

    @Override
    public void setAsActiveCell() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellAddress getAddress() {
        return delegate.getAddress();
    }

    @Override
    public void setCellComment(final Comment comment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comment getCellComment() {
        return delegate.getCellComment();
    }

    @Override
    public void removeCellComment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Hyperlink getHyperlink() {
        return delegate.getHyperlink();
    }

    @Override
    public void setHyperlink(final Hyperlink link) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeHyperlink() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRangeAddress getArrayFormulaRange() {
        return delegate.getArrayFormulaRange();
    }

    @Override
    public boolean isPartOfArrayFormulaGroup() {
        return delegate.isPartOfArrayFormulaGroup();
    }
}
