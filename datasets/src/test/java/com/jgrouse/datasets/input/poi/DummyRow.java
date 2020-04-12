package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.usermodel.*;

import java.util.Iterator;
import java.util.List;

import static com.jgrouse.util.Assert.isTrue;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

public class DummyRow implements Row {

    private final List<Cell> cells;
    private Sheet sheet;
    private int rowNum;

    public DummyRow(final List<Cell> cells) {
        this.cells = cells;
        int i = 0;
        for (final Cell cell : cells) {
            if (cell != null) {
                ((DummyCell) cell).fromRow(this).fromIndex(i);
            }
            i++;
        }
    }

    public DummyRow fromSheet(final Sheet sheet) {
        this.sheet = sheet;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public DummyRow fromRowNum(final int rowNum) {
        this.rowNum = rowNum;
        return this;
    }

    @Override
    public Cell createCell(final int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shiftCellsRight(final int firstShiftColumnIndex, final int lastShiftColumnIndex, final int step) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shiftCellsLeft(final int firstShiftColumnIndex, final int lastShiftColumnIndex, final int step) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cell createCell(final int column, final CellType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeCell(final Cell cell) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowNum(final int rowNum) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRowNum() {
        return rowNum;
    }

    @Override
    public Cell getCell(final int cellNum) {
        return retrieveCell(cellNum);
    }

    private Cell retrieveCell(final int cellNum) {
        return cellNum < cells.size() ? cells.get(cellNum) : null;
    }

    @Override
    public Cell getCell(final int cellNum, final MissingCellPolicy policy) {
        isTrue(policy == CREATE_NULL_AS_BLANK, () -> "Accepted policy is " + CREATE_NULL_AS_BLANK);
        Cell cell = retrieveCell(cellNum);
        if (cell == null) {
            cell = new DummyCell(CellType.BLANK).fromIndex(cellNum).fromRow(this);
        }
        return cell;
    }

    @Override
    public short getFirstCellNum() {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getLastCellNum() {
        return (short) cells.size();
    }

    @Override
    public int getPhysicalNumberOfCells() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeight(final short height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setZeroHeight(final boolean zHeight) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getZeroHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeightInPoints(final float height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getHeightInPoints() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFormatted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellStyle getRowStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowStyle(final CellStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Cell> cellIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sheet getSheet() {
        return sheet;
    }

    @Override
    public int getOutlineLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Cell> iterator() {
        return cells.iterator();
    }
}